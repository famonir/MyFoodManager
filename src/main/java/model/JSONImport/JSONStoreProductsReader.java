package model.JSONImport;

import controller.ExceptionControllers.ExceptionAlertController;
import model.Dao.FundamentalClasses.ProductDao;
import model.Dao.Stores.StoreDao;
import model.Dao.Stores.StoreProductsDAO;
import model.ExceptionMessaging;
import model.FundamentalClasses.Product;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

/**
 * Class used to read a json file with store products
 */
public class JSONStoreProductsReader {
    /** Method adding a product from a json file to the database
     * @param productName name of the product
     * @param productInfo json file content about the product
     * @param storeID ID of the store where the products are to be added
     * @throws ExceptionMessaging wrong json format exception
     */
    private void addProduct(String productName, JSONObject productInfo, int storeID) throws ExceptionMessaging {
        float price;
        String units;
        String brand;
        int quantity;
        try {
            price = productInfo.getFloat("price");
            units = productInfo.getString("units");
            brand = productInfo.getString("brand");
            quantity = productInfo.getInt("quantity");
        } catch (Exception throwables) {
            throw new ExceptionMessaging("Invalid product data for product: " + productName, throwables);
        }
        Product productFromDB = ProductDao.getInstance().get(productName);
        if (productFromDB == null) {
            throw new ExceptionMessaging("Product " + productName + " does not exist in the database");
        }
        Product productToAdd = new Product(productFromDB.getID(), productName, "", quantity, units);
        productToAdd.setPrice(price);
        productToAdd.setBrand(brand);
        StoreProductsDAO.getInstance().create(productToAdd, storeID);
    }

    /** Method retrieved all the data from a json file for store products
     * @param fileName path to the json file
     */
    public void fetchData(String fileName) {
        try {
            String contents = new String((Files.readAllBytes(Paths.get(fileName))));
            JSONObject o = new JSONObject(contents);
            int storeID = StoreDao.getInstance().getIdFromName(o.getString("name"));

            if (storeID > 0) {
                try {
                    JSONObject products = o.getJSONObject("products");
                    Iterator<?> keys = products.keys();
                    while(keys.hasNext()) {
                        String productName = (String) keys.next();
                        addProduct(productName, products.getJSONObject(productName), storeID);
                        }
                    }
                catch (Exception throwables) {
                    new ExceptionAlertController().showExceptionWindow(throwables);
                }
            } else {
                throw new ExceptionMessaging("No store named " + o.getString("name"));
            }

        } catch (Exception throwables) {
            new ExceptionAlertController().showExceptionWindow(new ExceptionMessaging("No store found with a name such as provided in the file"));
        }
    }
}
