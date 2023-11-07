package controller.MapControllers;

import controller.BaseControllers.MainAppController;
import controller.ResourcesAccess.Resources;
import controller.StoreControllers.SpecificStoreController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Dao.FundamentalClasses.ShoppingListDao;
import model.Dao.Stores.StoreDao;
import model.Dao.Stores.StoreProductsDAO;
import model.Dao.Stores.StoresLocationsDao;
import model.FundamentalClasses.Product;
import model.FundamentalClasses.ShoppingList;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Controller responsible for the map
 */
public class MapController extends MainAppController {
    private final static String UNUSEDSTRING = "unused";
    @FXML
    private ComboBox<String> shoppingListsComboBox;
    @FXML
    private WebView mapView;
    private double latitude;
    private double longitude;
    private List<Store> storeList;
    private final List<Store> availableStoresForSL = new ArrayList<>();
    private List<String> allCheapestStoresNames;
    /**Instance of the static bridge class used to connect the javascript files to the controller
     *
     */
    public static Bridge bridge;
    private static final String NoShoppingListString = "No shopping list";

    /**
     * Set up the map by using the method locateHome and put a home marker on the map
     */
    public void setupMap() {
        StoresLocationsDao storesLocationsDao = StoresLocationsDao.getInstance();
        storeList = storesLocationsDao.getAll();
        WebEngine engine = mapView.getEngine();
        engine.load("https://www.google.com/maps");
        try {
            latitude = user.getLatitude();
            longitude = user.getLongitude();
        }
        catch (Exception e) {
            latitude = 50.8130;
            longitude = 4.3823;
        }

        final URL urlGoogleMaps = getClass().getResource(Resources.MAPHTML.toString());
        assert urlGoogleMaps != null;
        engine.setJavaScriptEnabled(true);
        engine.load(urlGoogleMaps.toExternalForm());
        setupMarkers(engine);
        setEngineListener(engine);
        setupComboBox();
    }

    /**
     * Method taking the store names that were previously fetched from the DB into the StoreNames list and placing
     * them on the map
     */
    private void addAllStoresNamesToMap() {
        WebEngine engine = mapView.getEngine();
        StoreDao storeDao = StoreDao.getInstance();
        List<String> storesNames = storeDao.getAll();
        for (String storeName : storesNames) {
            engine.executeScript(String.format("mapHandler.addStoreName('%s')", storeName));
        }
        engine.executeScript("mapHandler.loadComboBox()");
    }

    private void deleteAllStoresMarkers(WebEngine engine) {
        engine.executeScript("mapHandler.deleteAllStoreMarkers()");
    }

    /**
     * Method taking the markers that were previously fetched from the DB into the storeList and placing them on the
     * map
     */
    private void addAllStoresMarkers(List<Store> stores) {
        WebEngine engine = mapView.getEngine();
        deleteAllStoresMarkers(engine);
        engine.executeScript("mapHandler.deletePolylines()");
        for (Store store : stores) {
            String functionName = "mapHandler.putStoreMarker(";
            // Displaying delete and products buttons in each popup
            String popup = store.getStoreName() + "<br> <input type=\"button\" value=\"Delete\" onclick=\"mapHandler.deleteMarker(" + store.getLat() +
                    ", " + store.getLng() + ")\"/>  <input type=\"button\" value=\"Products\"  onclick=\"mapHandler.displayProducts(\\'" + store.getStoreName() + "\\')\"/>";
            String functionToAddMarker = String.format("%s%s, %s, '%s')", functionName, store.getLat(), store.getLng(), popup);
            engine.executeScript(functionToAddMarker);
        }
    }

    /**
     * method setting up the comboBox
     */
    private void setupComboBox() {
        ShoppingListDao shoppingListDao = ShoppingListDao.getInstance();
        List<ShoppingList> shoppingLists = shoppingListDao.getAllUserShoppingLists(user.getID());
        this.shoppingListsComboBox.getItems().add(NoShoppingListString);
        for (ShoppingList shoppingList : shoppingLists) {
            this.shoppingListsComboBox.getItems().add(shoppingList.getName());
        }
        this.shoppingListsComboBox.getSelectionModel().select(0);
    }

    /**
     * method setting up the engine listener to allow cross java js communication
     */
    private void setEngineListener(WebEngine engine) {

        engine.getLoadWorker().stateProperty().addListener(
            (observableValue, state, t1) -> {
                bridge = new Bridge(mapView, this.storeList);
                JSObject win = (JSObject) engine.executeScript("window");
                win.setMember("bridge", bridge);
            }
        );
    }

    private void addStoreProduct(List<Product> storesProducts, Product bestProduct, int reachedQuantity) {
        boolean found = false;
        for (int i = 0; i < storesProducts.size(); ++i) {
            if (Objects.equals(storesProducts.get(i).getName(), bestProduct.getName())) {
                storesProducts.set(i, bestProduct);
                storesProducts.get(i).setQuantity(reachedQuantity);
                found = true;
                break;
            }
        } if (!found) {
            storesProducts.add(bestProduct);
            storesProducts.get(storesProducts.size()-1).setQuantity(reachedQuantity);
        }
    }

    private float calculatePrice(List<Product> allStoreProducts, ShoppingList shoppingList, String storeName) {
        float price = 0;
        float notFoundPrice = 100000f;
        List<Product> storesProducts = new ArrayList<>();
        for (Product shoppingListProduct : shoppingList.getProducts()) {
            float bestProductPrice = notFoundPrice;
            for (Product storeProduct : allStoreProducts) {
                if (Objects.equals(shoppingListProduct.getName(), storeProduct.getName())) {
                    int reachedQuantity = storeProduct.getQuantity();
                    int neededQuantity = shoppingListProduct.getQuantity();
                    int storeQuantity = reachedQuantity;
                    int multiplicator = 1;
                    while (reachedQuantity < neededQuantity) {
                        reachedQuantity += storeQuantity;
                        multiplicator += 1;
                    }
                    float productPrice = multiplicator * storeProduct.getPrice();
                    if (productPrice < bestProductPrice) {
                        bestProductPrice = productPrice;
                        addStoreProduct(storesProducts, storeProduct, multiplicator);
                    }
                }
            }
            price += bestProductPrice;
            if (bestProductPrice == notFoundPrice) {
                price = 0.f;
                break;
            }
        }
        if (price > 0.f) {
            setStoresProductsToSL(storesProducts, storeName);
        }
        return price;
    }

    private void setStoresProductsToSL(List<Product> storesProducts, String storeName) {
        for (Store store : storeList) {
            if (Objects.equals(store.getStoreName(), storeName)) {
                store.setProductsForSL(storesProducts);
            }
        }
    }

    private Store getStore(String storeName) {
        for (Store store : storeList) {
            if (Objects.equals(store.getStoreName(), storeName)) {
                return store;
            }
        }
        return null;
    }

    private void addAllAvailableStoresForSL(String storeName) {
        for (Store store : storeList) {
            if (Objects.equals(store.getStoreName(), storeName)) {
                availableStoresForSL.add(store);
            }
        }
    }

    private List<String> getAllAvailableStores(String shoppingListName) {
        availableStoresForSL.clear();
        StoreDao storeDao = StoreDao.getInstance();
        StoreProductsDAO storeProductsDAO = StoreProductsDAO.getInstance();
        ShoppingListDao shoppingListDao = ShoppingListDao.getInstance();
        ShoppingList shoppingList = shoppingListDao.get(shoppingListName, user.getID());
        List<String> storesNames = storeDao.getAll();
        float noProductsPrice = 1000000.f;
        List<String> cheapestStores = new ArrayList<>();
        float bestPrice = noProductsPrice;
        for (String store : storesNames) {
            List<Product> allStoreProducts = storeProductsDAO.getAllProducts(store);
            float storePrice = calculatePrice(allStoreProducts, shoppingList, store);
            if (storePrice != 0.f) {
                addAllAvailableStoresForSL(store);
                setStoresPrice(storePrice, store);
                if (storePrice < bestPrice) {
                    bestPrice = storePrice;
                    cheapestStores.clear();
                    cheapestStores.add(store);
                } else if (storePrice == bestPrice) {
                    cheapestStores.add(store);
                }
            } else {
                setStoresPrice(0f, store);
            }
        }
        return cheapestStores;
    }

    private void setStoresPrice(float newPrice, String storeName) {
        for (Store store : storeList) {
            if (Objects.equals(store.getStoreName(), storeName)) {
                store.setstorePriceForSL(newPrice);
            }
        }
    }

    private void findCheapestClosestStore() {
        if (allCheapestStoresNames != null) {
            WebEngine engine = mapView.getEngine();
            String cheapestStoresArray = "['" + allCheapestStoresNames.get(0) + "'";
            for (int i = 1; i < allCheapestStoresNames.size() - 1; ++i) {
                cheapestStoresArray = String.format("%s, '%s'", cheapestStoresArray, allCheapestStoresNames.get(i));
            }
            cheapestStoresArray = String.format("%s , '%s']", cheapestStoresArray, allCheapestStoresNames.get(allCheapestStoresNames.size() - 1));
            engine.executeScript(String.format("mapHandler.findClosestMarker(%s)", cheapestStoresArray));
        }
    }

    /**
     * Checks if a shopping list is selected and generates the stores markers adapted to it
     */
    public void getAccessToShoppingList() {
        String shoppingListName = shoppingListsComboBox.getSelectionModel().getSelectedItem();
        if (shoppingListName != null) {
            if (!shoppingListName.equals(NoShoppingListString)) {
                allCheapestStoresNames = getAllAvailableStores(shoppingListName);
                addAllStoresMarkers(availableStoresForSL);
            } else {
                for (Store store : storeList) {
                    store.setstorePriceForSL(0f);
                }
                addAllStoresMarkers(storeList);
            }
        }
    }

    /**
     * Class allowing the javascript files to send data to the controller
     */
    public class Bridge {
        WebView webView;
        private final List<Store> storeList;

        /**
         * Constructor, sets global vars
         */
        Bridge(WebView webView, List<Store> storeList) {
            this.webView = webView;
            this.storeList = storeList;
        }

        /**
         * delete a stroe from the map
         * @param longitude store longitude coordinate
         * @param latitude store latitude coordinate
         * @throws SQLException database exception
         */
        @SuppressWarnings(UNUSEDSTRING)
        public boolean deleteStore(double longitude, double latitude) throws SQLException {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete the store ?", ButtonType.YES, ButtonType.NO);

            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                Store storeToRemove = null;
                for (Store store : storeList) {
                    if (store.getLat() == latitude && store.getLng() == longitude) {
                        storeToRemove = store;
                        break;
                    }
                }
                if (storeToRemove != null) {
                    StoresLocationsDao storesLocationsDao = StoresLocationsDao.getInstance();
                    storesLocationsDao.delete(storeToRemove);
                    storeList.remove(storeToRemove);
                }
                return true;
            }
            return false;


        }

        /**
         *  Displays a new (pop-up) window with products of the chosen store
         * @param storeName the name of the store to display
         * @throws IOException thrown whenever a non-existing store occurs
         */
        @SuppressWarnings(UNUSEDSTRING)
        public void goToSpecificStoreMenu(String storeName) throws IOException {  // ignore the "never used" warning, it's used
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Resources.STOREPRODUCTLIST.toString()));
            Parent parent = loader.load();
            SpecificStoreController specificStoreController = loader.getController();
            specificStoreController.loadUser(user);
            specificStoreController.loadProductListView(Objects.requireNonNull(getStore(storeName)));
            specificStoreController.setShoppingListPriceLabel(shoppingListsComboBox.getSelectionModel().getSelectedItem());

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Products");
            stage.setScene(scene);
            stage.showAndWait();
        }

        /** Debugging method to interact from js to java console
         * @param message message from the map engine
         */
        @SuppressWarnings(UNUSEDSTRING)
        public void getCallBack(String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Data from javascript engine");
            alert.setContentText("message");
            alert.showAndWait();
        }

        /** Method to add a store from the map handler
         * @param lng longitude of the new store
         * @param lat latitude of the new store
         * @param storeName the name of the store
         * @throws SQLException exception from database
         */
        @SuppressWarnings(UNUSEDSTRING)
        public void addStore(double lng, double lat, String storeName) throws SQLException {
            Store storeToAdd = new Store(lng, lat, storeName);
            StoresLocationsDao.getInstance().create(storeToAdd);
            storeList.add(storeToAdd);
        }

    }

    /**
     * Method taking the store names that were previously fetched from the DB into the StoreNames list and placing
     * them on the map
     */
    private void setupMarkers(WebEngine engine) {
        engine.getLoadWorker().stateProperty().addListener((observableValue, state, t1) -> {
            engine.executeScript(String.format("mapHandler.locateHome(%s, %s)", latitude, longitude));
            addAllStoresNamesToMap();
            addAllStoresMarkers(storeList);
        });
    }

    /**
     * method used to find the nearest store depending on the user location
     */
    public void locateClosestStore() {
        WebEngine engine = mapView.getEngine();
        engine.executeScript("mapHandler.locateClosestStore()");
    }

    /**
     * method used to find the cheapest store
     */
    public void locateCheapestStore() {
        if (!Objects.equals(shoppingListsComboBox.getSelectionModel().getSelectedItem(), NoShoppingListString)) {
            findCheapestClosestStore();
        }
    }
}