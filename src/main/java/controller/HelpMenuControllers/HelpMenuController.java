package controller.HelpMenuControllers;

import controller.BaseControllers.MainAppController;
import controller.ResourcesAccess.Resources;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;

import java.awt.*;
import java.io.IOException;

/**Controller responsible for the whole help window
 *
 */
public class HelpMenuController extends MainAppController {
    @FXML
    private TextArea textArea;

    @FXML
    private Button registeringAndLoginButton;
    @FXML
    private Button shoppingListsButton;
    @FXML
    private Button recipesButton;
    @FXML
    private Button storesButton;
    @FXML
    private Button planningButton;
    @FXML
    private Button mapButton;


    /**
     * Changes the textArea content after button press
     * @param actionEvent button press event
     */
    public void onRegisteringAndLoginButton(ActionEvent actionEvent){
        textArea.setText("Registering & Login Tutorial:" +
                "\n\n-When launching the App, the user can choose between Login and Register" +
                "\n\n-If he chooses the first option, the user will be asked for his username and his password \n(the account" +
                " must already have been created.)" +
                "\n\n-The 'I have forgotten my password' option can be used to receive a new password by e-mail " +
                "\nafter entering the correct username and e-mail address" +
                "\n\n-The register option is used to create a new account." +
                "\n\n-The user needs to choose a username, a password, and to enter his name, surname and e-mail." +
                "\n he can also check the vegetarian box if he so desires" +
                "\n\n-After that a map will open and the user can click on it to choose his position");
    }

    /**
     * Changes the textArea content after button press
     * @param actionEvent button press event
     */
    public void onShoppingListButton(ActionEvent actionEvent){
        textArea.setText("Shopping Lists Tutorial:" +
                "\n\n-This window is used to create, modify, delete and archive the shopping lists." +
                "\n\n-The 'Add List' button creates a new shopping list after asking for a name and a date, " +
                "\nit will then be added to the" +
                "main frame with the other lists." +
                "\n\n-The user can also click on 2 filters (Vegetarian and Fish) if he only" +
                "wants to see the lists for one or both of those specific diets. " +
                "\n\n-The search bar can be used to find a list using its name." +
                "\n On the right of every list, there are 3 icons which represent the modify, delete and archive options");
    }

    /**
     * Changes the textArea content after button press
     * @param actionEvent button press event
     */
    public void onRecipeButton(ActionEvent actionEvent){
        textArea.setText("Recipes Tutorial:" +
                "\n\n-This window is used to create, modify and delete the known recipes." +
                "\n\n-A new recipe can be added to the list by using the 'Add Recipe' Button on the top left of the window" +
                "\n A name, a type and a number of people will be asked to the user." +
                "\n\n-To add the different ingredients to a recipe, you must click on the pencil icon on the right of the recipe" +
                "\n The ingredients must be added using the 'add ingredient' button and the quantities can be changed by " +
                "\n writing a new one or by using the + and - buttons. There is also the possibility of adding a description" +
                "\n\n-A recipe can be added to the favorites by clicking on the star next to the number of people." +
                "\n\n-The search bar can be used to find a recipe based on its name" +
                "\n\n-The Filters are used to find the recipes that fit a certain diet.");}

    /**
     * Changes the textArea content after button press
     * @param actionEvent button press event
     */
    public void onStoreButton(ActionEvent actionEvent){
        textArea.setText("Stores Tutorial:" +
                "\n\n-This window is used to see the different known stores and the products present in each" +
                "\n\n-The 'New Store' button is used to add a new store to the list on the left after entering its name" +
                "\n\n-the user has the possibility to search for a specific store with the search bar on the top of the list" +
                "\n-If a store is clicked on, the different available products will be shown on the right" +
                "\n\n-Using the 'New Product' button, a product can be added to a store after entering his price, the brand," +
                "\n the quantity, the units and the type of the product" +
                "\n\n-The two icons on the right of every product are used to modify or delete a product.");
    }

    /**
     * Changes the textArea content after button press
     * @param actionEvent button press event
     */
    public void onPlanningButton(ActionEvent actionEvent){
        textArea.setText("Planning Tutorial:" +
                "\n\n-This window is used to create a food planning for a week." +
                "\n\n-The user can change the week by using the date picker on the top right" +
                "\n\n-A recipe can be manually added to a day by clicking the Add recipe button at the bottom of the day" +
                "\n column. The user should only enter the number of people for each recipe." +
                "\n\n-The 'Generate a food planning for this week' button will ask the user for his preferences for the week" +
                "\n (vegetarian, fish, how many dinners, how many lunches...) and automatically create a food planning for the week" +
                "\n\n-The 'Generate Shopping list' Button automatically creates a shopping list with all the needed ingredients for" +
                "\n the food planning of the selected week. The date of the week is used for the name of the shopping list");
    }

    /**
     * Changes the textArea content after button press
     * @param actionEvent button press event
     */
    public void onMapButton(ActionEvent actionEvent){
        textArea.setText("Map Tutorial:" +
                "\n\n-This window is used to see the different known stores on a map and to locate the nearest or the cheapest one" +
                "\n\n-After clicking on a store, the user can choose to either see the products available in it or delete it" +
                "\n\n-You can also add a new store on the map by clicking on the location of the store, choosing its name" +
                "\n in the 'Store Name' menu on the top of the map and clicking save" +
                "\n\n-If a shopping list is chosen on the top center of the window, the user can find the nearest or the cheapest" +
                "\n store where all the products in the shopping list are available");
    }
}
