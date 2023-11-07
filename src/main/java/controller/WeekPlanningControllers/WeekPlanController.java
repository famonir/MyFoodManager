package controller.WeekPlanningControllers;

import controller.ExceptionControllers.ExceptionAlertController;
import controller.ResourcesAccess.Images;
import controller.BaseControllers.MainAppController;
import controller.ResourcesAccess.Resources;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.FundamentalClasses.Product;
import model.Dao.Planning.DayPlanningDao;
import model.Dao.FundamentalClasses.ShoppingListDao;
import model.Planning.DayPlanning;
import model.FundamentalClasses.Recipe;
import model.FundamentalClasses.ShoppingList;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Class controlling all the methods on the FXML view of the week planning window
 */
public class WeekPlanController extends MainAppController {
    @FXML
    private Label mondayDateLabel;
    @FXML
    private Label tuesdayDateLabel;
    @FXML
    private Label wednesdayDateLabel;
    @FXML
    private Label thursdayDateLabel;
    @FXML
    private Label fridayDateLabel;
    @FXML
    private Label saturdayDateLabel;
    @FXML
    private Label sundayDateLabel;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ListView<VBox> mondayListView;
    @FXML
    private ListView<VBox> tuesdayListView;
    @FXML
    private ListView<VBox> wednesdayListView;
    @FXML
    private ListView<VBox> thursdayListView;
    @FXML
    private ListView<VBox> fridayListView;
    @FXML
    private ListView<VBox> saturdayListView;
    @FXML
    private ListView<VBox> sundayListView;
    private Image deleteImage;
    private Image viewImage;
    private Image minusImage;
    private Image plusImage;

    private final List<Label> days = new ArrayList<>();
    private final List<ListView<VBox>> daysListViews = new ArrayList<>();
    private final List<DayPlanning> plannedDays = new ArrayList<>();
    private String lastQuantityInput;
    private static final int DAYS = 7;
    private static final int DISHTYPELABELWIDTH = 90;
    private static final int NAMELABELWIDTH = 118;
    private static final int QUANTITYWIDTH = 38;
    private static final int HBOXHEIGHT = 10;

    private static final int MONTHSUBSTRINGUPPER = 5;
    private static final int MONTHSUBSTRINGLOWER = 3;
    private static final int YEARSUBSTRINGUPPER = 10;
    private static final int YEARSUBSTRINGLOWER = 6;

    /**
     * Method to get monday date label
     * @return Label
     */
    public Label getMondayDateLabel() {
        return mondayDateLabel;
    }

    /**
     * Method to get Tuesday date label
     * @return Label
     */
    public Label getTuesdayDateLabel() {
        return tuesdayDateLabel;
    }

    /**
     * Method to get Wednesday date label
     * @return Label
     */
    public Label getWednesdayDateLabel() {
        return wednesdayDateLabel;
    }

    /**
     * Method to get Thursday date label
     * @return Label
     */
    public Label getThursdayDateLabel() {
        return thursdayDateLabel;
    }

    /**
     * Method to get Friday date label
     * @return Label
     */
    public Label getFridayDateLabel() {
        return fridayDateLabel;
    }

    /**
     * Method to get Saturday date label
     * @return Label
     */
    public Label getSaturdayDateLabel() {
        return saturdayDateLabel;
    }

    /**
     * Method to get Sunday date label
     * @return Label
     */
    public Label getSundayDateLabel() {
        return sundayDateLabel;
    }

    /**
     * Method to get planned days
     * @return list of the days planned by the user
     */
    public List<DayPlanning> getPlannedDays() {
        return plannedDays;
    }

    /**
     * Method to set days array
     */
    public void setDaysArrays() {
        days.add(mondayDateLabel);
        days.add(tuesdayDateLabel);
        days.add(wednesdayDateLabel);
        days.add(thursdayDateLabel);
        days.add(fridayDateLabel);
        days.add(saturdayDateLabel);
        days.add(sundayDateLabel);
        daysListViews.add(mondayListView);
        daysListViews.add(tuesdayListView);
        daysListViews.add(wednesdayListView);
        daysListViews.add(thursdayListView);
        daysListViews.add(fridayListView);
        daysListViews.add(saturdayListView);
        daysListViews.add(sundayListView);
    }

    /**
     * Method to load images
     * @throws IOException input expection which might occur from the FileInputStream
     */
    private void loadImages() throws IOException {
        String pathToView = Images.VIEW.toString();
        String pathToDelete = Images.DELETE.toString();
        String pathToMinus = Images.MINUS.toString();
        String pathToPlus = Images.PLUS.toString();
        try (FileInputStream viewInput = new FileInputStream(pathToView);
             FileInputStream deleteInput = new FileInputStream(pathToDelete);
             FileInputStream minusInput = new FileInputStream(pathToMinus);
             FileInputStream plusInput = new FileInputStream(pathToPlus)) {
            viewImage = new Image(viewInput);
            deleteImage = new Image(deleteInput);
            plusImage = new Image(plusInput);
            minusImage = new Image(minusInput);
        }
    }

    /**
     * Method to setUp button
     * @param buttonImage image of the button
     * @param buttonDimensions dimensions of the button
     * @return button
     */
    private Button setupButton(Image buttonImage, int buttonDimensions) {
        Button buttonToSet = new Button();
        ImageView buttonImgView = new ImageView(buttonImage);
        buttonImgView.setFitHeight(buttonDimensions); buttonImgView.setFitWidth(buttonDimensions);
        buttonToSet.setGraphic(buttonImgView);
        buttonToSet.setPrefSize(buttonDimensions, buttonDimensions);

        return buttonToSet;
    }

    /**
     * Method to set the actions after the press of the delete button
     * @param delete delete button
     * @param recipe recipe to be deleted
     * @param dayPlanning dayplanning from which the recipe is deleted
     * @param listView ListView
     * @param finalVBox Vbox to be deleted from the listView
     */
    protected void setDeleteButtonCallback(Button delete, Recipe recipe, DayPlanning dayPlanning, ListView<VBox> listView, VBox finalVBox) {
        delete.setOnAction(e -> {
            try {
                dayPlanning.removeRecipe(recipe, dayPlanning.getID());
                listView.getItems().remove(finalVBox);
            } catch (SQLException throwables) {
                new ExceptionAlertController().showExceptionWindow(throwables);
            }
        });
    }

    /**
     * Method to setup top Hbox
     * @param recipe recipe from which the type will be taken
     * @param hBoxHeight height of the hbox
     * @param dayPlanning dayPlanning from which the recipe would be deleted when delete button pressed
     * @param listView listView
     * @param finalVBox Vbox which would be deleted from the listView when delete button pressed
     * @return HBox after the setup
     */
    private HBox setupTopHBox(Recipe recipe, int hBoxHeight, DayPlanning dayPlanning, ListView<VBox> listView, VBox finalVBox) {
        Label dishTypeLabel = new Label(recipe.getDishType());
        dishTypeLabel.setPrefSize(DISHTYPELABELWIDTH, hBoxHeight);
        Button deleteButton = setupButton(deleteImage, hBoxHeight);
        setDeleteButtonCallback(deleteButton, recipe, dayPlanning, listView, finalVBox);

        return new HBox(dishTypeLabel, deleteButton);
    }

    /**
     * Method to setup middle HBox 
     * @param recipe recipe, used to get its name to setup the lable
     * @param hBoxHeight height of the middle Hbox
     * @return HBox
     */
    private HBox setupMiddleHBox(Recipe recipe, int hBoxHeight) {
        Label recipeNameLabel = new Label(recipe.getName());
        recipeNameLabel.setPrefSize(NAMELABELWIDTH, hBoxHeight);

        return new HBox(recipeNameLabel);
    }

    /**
     * Method to set minus button callBack
     * @param minusButton minus button
     * @param recipe recipe associated with the minus button to retrieve number of people
     * @param dayPlanning dayPlanning where this will be shown
     * @param quantityTextField Textfield for the quantity
     */
    private void setMinusButtonCallback(Button minusButton, Recipe recipe, DayPlanning dayPlanning, TextField quantityTextField) {
        minusButton.setOnAction(e -> {
            if (recipe.getPeopleCount() > 1) {
                try {
                    recipe.setPeopleCountNoDBUpdate(recipe.getPeopleCount() - 1, dayPlanning.getID());
                    quantityTextField.setText(String.valueOf(recipe.getPeopleCount()));
                } catch (SQLException throwables) {
                    new ExceptionAlertController().showExceptionWindow(throwables);
                }
            }
        });
    }

    /**
     * Method to set plusButton callBack
     * @param plusButton plus button
     * @param recipe recipe associated with the minus button to retrieve number of people
     * @param dayPlanning dayPlanning where this will be shown
     * @param quantityTextField Textfield for the quantity
     */
    private void setPlusButtonCallback(Button plusButton, Recipe recipe, DayPlanning dayPlanning, TextField quantityTextField) {
        plusButton.setOnAction(e -> {
            try {
                recipe.setPeopleCountNoDBUpdate(recipe.getPeopleCount() + 1, dayPlanning.getID());
                quantityTextField.setText(String.valueOf(recipe.getPeopleCount()));
            } catch (SQLException throwables) {
                new ExceptionAlertController().showExceptionWindow(throwables);
            }
        });
    }

    /**
     * Method to set quantity button mouse onMouse Actions
     * @param quantityTextField Textfield for the quantity
     */
    private void setQuantityButtonMouseCallbacks(TextField quantityTextField) {
        quantityTextField.setOnMouseEntered(e -> {
            quantityTextField.positionCaret(quantityTextField.getText().length());
            quantityTextField.setEditable(true);
        });
        quantityTextField.setOnMouseExited(e -> {
            quantityTextField.positionCaret(quantityTextField.getText().length());
            quantityTextField.setEditable(false);
        });
    }

    /**
     * Method to set quantity button onKey actions
     * @param quantityTextField textField of the Quantity
     * @param recipe recipe associated with the quantity
     * @param dayPlanning dayPlanning containing the recipe and therefore the quantity
     */
    private void setQuantityButtonKeyCallbacks(TextField quantityTextField, Recipe recipe, DayPlanning dayPlanning) {
        quantityTextField.setOnKeyPressed(e -> lastQuantityInput = quantityTextField.getText());

        quantityTextField.setOnKeyReleased(e -> {
            try {
                int newQuantity = Integer.parseInt(quantityTextField.getText());
                recipe.setPeopleCountNoDBUpdate(newQuantity, dayPlanning.getID());
            } catch (Exception throwables) {
                quantityTextField.setText(lastQuantityInput);}
            quantityTextField.positionCaret(quantityTextField.getText().length());
        });
    }

    /**
     * Method to call the quantity Textfield's onActions methods
     * @param quantityTextField textField of the Quantity
     * @param recipe recipe associated with the quantity
     * @param dayPlanning dayPlanning containing the recipe and therefore the quantity
     */
    private void setQuantityButtonCallbacks(TextField quantityTextField, Recipe recipe, DayPlanning dayPlanning) {
        setQuantityButtonMouseCallbacks(quantityTextField);
        setQuantityButtonKeyCallbacks(quantityTextField, recipe, dayPlanning);
    }

    /**
     * Method to set the viewButton's onActions method
     * @param viewButton  view button
     * @param recipe recipe associated with the view button
     */
    private void setViewButtonCallbacks(Button viewButton, Recipe recipe) {
        viewButton.setOnAction(e -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Resources.RECIPEPREVIEW.toString()));
            try {
                Parent parent = loader.load();
                RecipePreviewController recipePreviewController = loader.getController();
                recipePreviewController.loadUser(user);
                recipePreviewController.loadRecipe(recipe);
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Preview : " + recipe.getName());
                stage.setScene(scene);
                stage.setResizable(false);
                stage.showAndWait();
            } catch (IOException throwables) {
                new ExceptionAlertController().showExceptionWindow(throwables);
            }
        });
    }

    /**
     * method to setup BottomHBox
     * @param recipe recipe to associate with every button
     * @param hBoxHeight height of the Hbox
     * @param dayPlanning dayplanning where all this is shown
     * @return HBox
     */
    private HBox setupBottomHBox(Recipe recipe, int hBoxHeight, DayPlanning dayPlanning) {
        Button minusButton = setupButton(minusImage, hBoxHeight);
        TextField quantityTextField = new TextField();
        quantityTextField.setText(String.valueOf(recipe.getPeopleCount()));
        quantityTextField.setPrefSize(QUANTITYWIDTH, hBoxHeight);
        Button plusButton = setupButton(plusImage, hBoxHeight);
        Button viewButton = setupButton(viewImage, hBoxHeight);
        viewButton.setPrefSize(hBoxHeight, hBoxHeight);
        setMinusButtonCallback(minusButton, recipe, dayPlanning, quantityTextField);
        setPlusButtonCallback(plusButton, recipe, dayPlanning, quantityTextField);
        setQuantityButtonCallbacks(quantityTextField, recipe, dayPlanning);
        setViewButtonCallbacks(viewButton, recipe);

        return new HBox(minusButton, quantityTextField, plusButton, viewButton);
    }

    /**
     * Method to setup listView
     * @param listView listView
     * @param dayPlanning dayplanning
     */
    private void setupListView(ListView<VBox> listView, DayPlanning dayPlanning) {
        int hBoxHeight = HBOXHEIGHT;
        for (Recipe recipe : dayPlanning.getRecipes()) {
            VBox finalVBox = new VBox();
            HBox topRecipeHBox = setupTopHBox(recipe, hBoxHeight, dayPlanning, listView, finalVBox);
            HBox midRecipeHBox = setupMiddleHBox(recipe, hBoxHeight);
            HBox bottomRecipeHBox = setupBottomHBox(recipe, hBoxHeight, dayPlanning);
            finalVBox.getChildren().add(topRecipeHBox);
            finalVBox.getChildren().add(midRecipeHBox);
            finalVBox.getChildren().add(bottomRecipeHBox);
            listView.getItems().add(finalVBox);
        }
    }

    /**
     * Method to set listViews
     * @throws SQLException sql exception which could occur when calling DayPlanningDao.getInstance()
     */
    protected void setListViews() throws SQLException {
        plannedDays.clear();
        DayPlanningDao dayPlanningDao = DayPlanningDao.getInstance();
        for (int dayIndex = 0; dayIndex < DAYS; ++dayIndex) {
            daysListViews.get(dayIndex).getItems().clear();
            DayPlanning weekDayPlanning = dayPlanningDao.get(user.getID(), days.get(dayIndex).getText());
            plannedDays.add(weekDayPlanning);
            setupListView(daysListViews.get(dayIndex), weekDayPlanning);
        }
    }

    /**
     * Method to cycle days
     * @param calendar calendar
     * @throws SQLException sql exception which could occur when calling setListViews()
     */
    private void cycleDays(Calendar calendar) throws SQLException {
        String pattern = "dd.MM.yyyy";
        SimpleDateFormat dateFormater = new SimpleDateFormat(pattern);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        for (int dayIndex = 0; dayIndex < DAYS; ++dayIndex) {
            days.get(dayIndex).setText((dateFormater.format(calendar.getTime())));
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }
        setListViews();
    }

    /**
     * Method to load days labels
     * @throws IOException input exception which could occur when calling loadImages()
     * @throws SQLException sql exception which could occur when calling cycleDays()
     */
    public void loadDaysLabels() throws IOException, SQLException {
        loadImages();
        setDaysArrays();
        Calendar calendar = Calendar.getInstance();
        cycleDays(calendar);
    }

    /**
     * Method to set calendar
     * @param dateString string for the date
     * @return calendar
     * @throws ParseException parse exception which could occur when calling the parse() method
     */
    private Calendar setCalendar(String dateString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date date = formatter.parse(dateString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar;
    }

    /**
     * Method to go to the previous week
     * @throws ParseException parse exception which could occur when calling the parse() method
     * @throws SQLException sql exception which could occur when calling the cycleDays() method
     */
    public void goPreviousWeek() throws ParseException, SQLException {
        Calendar calendar = setCalendar(mondayDateLabel.getText());
        int today = calendar.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek();
        calendar.add(Calendar.DATE, - today - DAYS);
        cycleDays(calendar);
    }

    /**
     * Method to go to the next week
     * @throws ParseException parse exception which could occur when calling the setCalendar() method
     * @throws SQLException sql exception which could occur when calling the cycleDays() method
     */
    public void goNextWeek() throws ParseException, SQLException {
        Calendar calendar = setCalendar(mondayDateLabel.getText());
        int today = calendar.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek();
        calendar.add(Calendar.DATE, - today + DAYS);
        cycleDays(calendar);
    }

    /**
     * Method to add a recipe to the Day
     * @param dayIndex index of the day
     */
    protected void addRecipeToDay(int dayIndex) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Resources.NEWRECIPETOPLAN.toString()));
        try {
            Parent parent = loader.load();
            AddRecipeToPlanningController addRecipeToPlanningController = loader.getController();
            addRecipeToPlanningController.loadUser(user);
            addRecipeToPlanningController.loadDayPlanning(plannedDays.get(dayIndex));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("New Recipe : " + plannedDays.get(dayIndex).getDate());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
            setListViews();
        } catch (IOException | SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    /**
     * Method to add recipe monday
     */
    public void addRecipeMonday() {
        addRecipeToDay(DaysEnum.MONDAY.getDayIndex());
    }

    /**
     * Method to add recipe tuesday
     */
    public void addRecipeTuesday() {
        addRecipeToDay(DaysEnum.TUESDAY.getDayIndex());
    }

    /**
     * Method to add recipe wednesday
     */
    public void addRecipeWednesday() {
        addRecipeToDay(DaysEnum.WEDNESDAY.getDayIndex());
    }

    /**
     * Method to add recipe thursday
     */
    public void addRecipeThursday() {
        addRecipeToDay(DaysEnum.THURSDAY.getDayIndex());
    }

    /**
     * Method to add recipe friday
     */
    public void addRecipeFriday() {
        addRecipeToDay(DaysEnum.FRIDAY.getDayIndex());
    }

    /**
     * Method to add recipe saturday
     */
    public void addRecipeSaturday() {
        addRecipeToDay(DaysEnum.SATURDAY.getDayIndex());
    }

    /**
     * Method to add recipe sunday
     */
    public void addRecipeSunday() {
        addRecipeToDay(DaysEnum.SUNDAY.getDayIndex());
    }

    /**
     * method datePicker action
     * @throws ParseException parse exception which could occur when calling the setCalendar() method
     * @throws SQLException sql exception which could occur when calling the cycleDays() method
     */
    public void datePickerAction() throws ParseException, SQLException {
        String selectedDay = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        Calendar calendar = setCalendar(selectedDay);
        cycleDays(calendar);
        for (Label dayLabel : days) {
            if (Objects.equals(dayLabel.getText(), selectedDay)) {
                dayLabel.setStyle("-fx-background-color : rgba(0, 200, 256, 1);");
            } else {
                dayLabel.setStyle(null);
            }
        }
    }

    /**
     * Method to get shopping list date
     * @param dateWeekPlanFormat string of the format of the date week plan
     * @return string of the date in a new format form
     */
    private String getShoppingListDate(String dateWeekPlanFormat) {
        String day = dateWeekPlanFormat.substring(0, 2);
        String month = new DateFormatSymbols().getMonths()[Integer.parseInt(dateWeekPlanFormat.substring(MONTHSUBSTRINGLOWER, MONTHSUBSTRINGUPPER)) -1];
        String year = dateWeekPlanFormat.substring(YEARSUBSTRINGLOWER, YEARSUBSTRINGUPPER);

        return String.format("%s %s %s", day, month, year);
    }

    /**
     * Method to add products to shopping list
     * @param allProducts list of all the products to be added to the shopping list
     * @param shoppingListToAdd shoppingList in which products will be added
     */
    private void addProductsToShoppingList(List<Product> allProducts, ShoppingList shoppingListToAdd) {
        for (Product product : allProducts) {
            shoppingListToAdd.addProduct(product);
        }
    }

    /**
     * Method to generate products to shopping list
     * @param shoppingListToAdd shoppingList in which products will be added
     */
    private void generateProductsToShoppingList(ShoppingList shoppingListToAdd) {
        List<Product> allProducts = new ArrayList<>();
        for (DayPlanning dayPlanning : plannedDays) {
            for (Recipe recipe : dayPlanning.getRecipes()) {
                for (Product product : recipe.getProducts()) {
                    boolean alreadyPresent = false;
                    for (Product alreadyAddedProduct : allProducts) {
                        if (Objects.equals(alreadyAddedProduct.getName(), product.getName())) {
                            alreadyAddedProduct.setQuantity(alreadyAddedProduct.getQuantity() + product.getQuantity());
                            alreadyPresent = true;
                            break;
                        }
                    }
                    if (!alreadyPresent) {
                        allProducts.add(product);
                    }
                }
            }
        }
        addProductsToShoppingList(allProducts, shoppingListToAdd);
    }

    /**
     * Method to send successful add dialog
     * @param shoppingListName string of the name of the shopping list
     */
    private void sendSuccessfulAddDialog(String shoppingListName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(shoppingListName);
        alert.setHeaderText(null);
        alert.setContentText("Shopping List successfully generated.\nPlease check out the Shopping List window.");
        alert.showAndWait();
    }

    /**
     * Method to generate shopping list
     * @throws SQLException sql exception which could occur when the create() method is called
     */
    public void generateShoppingList() throws SQLException {
        String shoppingListName = String.format("Auto-generation : week of %s", mondayDateLabel.getText());
        ShoppingList shoppingListToAdd = new ShoppingList(0, shoppingListName,
                user.getID(), getShoppingListDate(mondayDateLabel.getText()), false);
        ShoppingListDao.getInstance().create(shoppingListToAdd);
        shoppingListToAdd = ShoppingListDao.getInstance().get(shoppingListName, user.getID());
        generateProductsToShoppingList(shoppingListToAdd);
        sendSuccessfulAddDialog(shoppingListName);
    }

    /**
     * Method to allow generation
     * @return boolean
     */
    private boolean allowGeneration() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Food planning auto-generation");
        alert.setHeaderText("Going to auto-generation will erase your whole plan for this week.");
        alert.setContentText("Are you sure you want to automatically generate this week?");
        Optional<ButtonType> result = alert.showAndWait();

        return result.filter(buttonType -> buttonType == ButtonType.OK).isPresent();
    }

    /**
     * Method to set generation window labels
     * @param autoGenerateWeekController controller used to get the dates labels and to change their text
     */
    private void setGenerationWindowLabels(AutoGenerateWeekController autoGenerateWeekController) {
        autoGenerateWeekController.getMondayDateLabel().setText(mondayDateLabel.getText());
        autoGenerateWeekController.getTuesdayDateLabel().setText(tuesdayDateLabel.getText());
        autoGenerateWeekController.getWednesdayDateLabel().setText(wednesdayDateLabel.getText());
        autoGenerateWeekController.getThursdayDateLabel().setText(thursdayDateLabel.getText());
        autoGenerateWeekController.getFridayDateLabel().setText(fridayDateLabel.getText());
        autoGenerateWeekController.getSaturdayDateLabel().setText(saturdayDateLabel.getText());
        autoGenerateWeekController.getSundayDateLabel().setText(sundayDateLabel.getText());
    }

    /**
     * Method to menu generation
     * @param actionEvent action throwing the button pressed, used to pass to the next window
     * @throws IOException io exception which could occur when the setRoot(), loadDaysLabels and loadSideBar()
     * methods are calleld
     * @throws SQLException sql exception which could occur when the loadDaysLabels(), setListViews() methods are called
     */
    public void goToMenuGeneration(ActionEvent actionEvent) throws IOException, SQLException {
        if (allowGeneration()) {
            FXMLLoader loader = setRoot(Resources.AUTOGENERATION.toString());
            AutoGenerateWeekController autoGenerateWeekController = loader.getController();
            autoGenerateWeekController.loadUser(user);
            autoGenerateWeekController.loadDaysLabels();
            autoGenerateWeekController.loadSideBar(loader);
            autoGenerateWeekController.setDaysArrays();
            setGenerationWindowLabels(autoGenerateWeekController);
            autoGenerateWeekController.setListViews();
            autoGenerateWeekController.eraseAllRecipes();
            autoGenerateWeekController.setListViews();
            setStage(actionEvent);
        }
    }
    /**
     * Method to get delete Image
     * @return Image
     */
    public Image getDeleteImage() {
        return deleteImage;
    }
}
