package controller.ResourcesAccess;


/**
 * Enum type with all the resources to the fxml files
 */
public enum Resources {

    /**
     * Main Path
     */
    MAINPATH {public String toString() {return "/fxml/";}},

    /**
     * Html Path
     */
    HTMLPATH {public String toString() {return "/html/";}},

    /**
     * StarMenu Path
     */
    STARTMENU {public String toString() {return MAINPATH + "PreLoginView/StartMenuView.fxml";}},

    /**
     * LoginMenu Path
     */
    LOGINMENU {public String toString() {return MAINPATH + "PreLoginView/LoginMenuView.fxml";}},

    /**
     * RegisterMenu Path
     */
    REGISTERMENU {public String toString() {return MAINPATH + "PreLoginView/RegisterMenuView.fxml";}},

    /**
     * PasswordMenu Path
     */
    PASSWORDMENU {public String toString() {return MAINPATH + "PreLoginView/ForgottenPasswordView.fxml";}},

    /**
     * MainMenu Path
     */
    MAINMENU {public String toString() {return MAINPATH + "MainMenuView.fxml";}},

    /**
     * Help Path
     */
    HELP {public String toString() {return MAINPATH + "HelpView.fxml";}},

    /**
     * ShoppingMenu Path
     */
    SHOPPINGMENU {public String toString() {return MAINPATH + "ShoppingListView/ShoppingListView.fxml";}},

    /**
     * ArchivesMenu Path
     */
    ARCHIVESMENU {public String toString() {return MAINPATH + "ShoppingListView/ShoppingListArchivesView.fxml";}},

    /**
     * ListMenu Path
     */
    LISTMENU {public String toString() {return MAINPATH + "ShoppingListView/ShoppingListIngredientsView.fxml";}},

    /**
     * PreviewMenu Path
     */
    PREVIEWMENU {public String toString() {return MAINPATH + "ShoppingListView/PdfPreviewView.fxml";}},

    /**
     * AddListMenu Path
     */
    ADDLISTMENU {public String toString() {return MAINPATH + "ShoppingListView/NewShoppingListView.fxml";}},

    /**
     * AddProductMenu Path
     */
    ADDPRODUCTMENU {public String toString() {return MAINPATH + "ShoppingListView/ProductsToShoppingListView.fxml";}},

    /**
     * EmailMenu Path
     */
    EMAILMENU {public String toString() {return MAINPATH + "ShoppingListView/ShoppingListEmailView.fxml";}},

    /**
     * RecipesMenu Path
     */
    RECIPESMENU {public String toString() {return MAINPATH + "RecipeView/RecipeView.fxml";}},

    /**
     * RecIngrMenu Path
     */
    RECINGRMENU {public String toString() {return MAINPATH + "RecipeView/RecipeIngredientsView.fxml";}},

    /**
     * AddRecipe Path
     */
    ADDRECIPE {public String toString() {return MAINPATH + "RecipeView/NewRecipeView.fxml";}},

    /**
     * AddIngredientMenu Path
     */
    ADDINGREDIENTMENU {public String toString() {return MAINPATH + "RecipeView/ProductsToRecipeView.fxml";}},

    /**
     * ProfileMenu Path
     */
    PROFILEMENU {public String toString() {return MAINPATH + "ProfileView/ProfileMenuView.fxml";}},

    /**
     * PlanningMenu Path
     */
    PLANNINGMENU {public String toString() {return MAINPATH + "WeekPlanningView/MenuPlanningView.fxml";}},

    /**
     * RecipePreview Path
     */
    RECIPEPREVIEW {public String toString() {return MAINPATH + "WeekPlanningView/RecipePreviewView.fxml";}},

    /**
     * NewRecipePlan Path
     */
    NEWRECIPETOPLAN {public String toString() {return MAINPATH + "WeekPlanningView/AddRecipeToPlanningView.fxml";}},

    /**
     * AutoGeneration Path
     */
    AUTOGENERATION {public String toString() {return MAINPATH + "WeekPlanningView/AutoGenerateWeekView.fxml";}},

    /**
     * Map Path
     */
    MAP {public String toString() {return MAINPATH + "MapView/MapView.fxml";}},

    /**
     * Sidebar Path
     */
    SIDEBAR {public String toString() {return MAINPATH + "SideBar.fxml";}},

    /**
     * MapHtml Path
     */
    MAPHTML {public String toString() {return HTMLPATH + "Map.html";}},

    /**
     * HomeLocationMenu Path
     */
    HOMELOCATIONMENU {public String toString() {return MAINPATH + "ProfileView/HomeLocationView.fxml";}},

    /**
     * HomeLocationMapHtml Path
     */
    HOMELOCATIONMAPHTML {public String toString() {return HTMLPATH + "HomeLocationMap.html";}},

    /**
     * Stores Path
     */
    STORES {public String toString() {return MAINPATH + "StoresView/StoresView.fxml";}},

    /**
     * StoreProductList Path
     */
    STOREPRODUCTLIST {public String toString() {return MAINPATH + "StoresView/SpecificStoreView.fxml";}},

    /**
     * NewStore Path
     */
    NEWSTORE {public String toString() {return MAINPATH + "StoresView/NewStoreView.fxml";}},

    /**
     * NewStoreProduct Path
     */
    NEWSTOREPRODUCT {public String toString() {return MAINPATH + "StoresView/NewProductView.fxml";}},

    /**
     * EditStoreProduct Path
     */
    EDITSTOREPRODUCT {public String toString() {return MAINPATH + "StoresView/EditProductView.fxml";}},
}
