package model.Dao.FundamentalClasses;

import controller.ExceptionControllers.ExceptionAlertController;
import model.Dao.Dao;
import model.Dao.RecipeDao.RecipeListDao;
import model.FundamentalClasses.Product;
import model.FundamentalClasses.ProductPerList;
import model.FundamentalClasses.ShoppingList;

import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
/**
 * Shopping list DAO class
 */
public final class ShoppingListDao extends Dao<ShoppingList> {
    private static final String NAMECOLUMN = "name";
    private static final String USERCOLUMN = "user";
    private static final String DATECOLUMN = "date";
    private static final String ARCHIVECOLUMN = "archived";

    private static ShoppingListDao singletonInstance = null;

    private ShoppingListDao() {}

    /**
     * Singleton instance getter
     * @return instance
     */
    public static ShoppingListDao getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new ShoppingListDao();
        }

        return singletonInstance;
    }

    /**
     * Mandatory override
     * @return null
     */
    @Override
    public List<ShoppingList> getAll(){ return null;}

    /**
     * Method that gets all shopping lists of a user
     * @param idUser the database id of the user
     * @return a list of ShoppingList objects
     */
    public List<ShoppingList> getAllUserShoppingLists(int idUser) {
        List<ShoppingList> shoppingLists = new ArrayList<>();
        String query = String.format("SELECT * FROM shopping_list WHERE user = %d AND archived = 0;", idUser);

        return getShoppingLists(shoppingLists, query);
    }

    /**
     * This method gets a list of shopping lists
     * @param shoppingLists shoppingList list
     * @param query string of the query
     * @return List<ShoppingList>
     */
    private List<ShoppingList> getShoppingLists(List<ShoppingList> shoppingLists, String query) {
        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            if (queryResult != null) {
                while (queryResult.next()) {
                    ShoppingList nextShoppingList = new ShoppingList(queryResult.getInt("id"),
                            queryResult.getString(NAMECOLUMN),
                            queryResult.getInt(USERCOLUMN),
                            queryResult.getString(DATECOLUMN),
                            (queryResult.getInt(ARCHIVECOLUMN) == 1));
                    shoppingLists.add(nextShoppingList);
                }
            }
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

        return shoppingLists;
    }

    @Override
    public ShoppingList get(String nameShoppingList) {
        return null;
    }

    /**
     * Method responsible for retrieving a shopping list by name
     * @param nameShoppingList the name of the shopping list
     * @param userID ID of the user
     * @return a ShoppingList object with all its attributes
     */
    public ShoppingList get(String nameShoppingList, int userID) {
        String query = String.format("SELECT * FROM shopping_list WHERE name = '%s' AND user = %d;", nameShoppingList, userID);

        return getShoppingList(query);
    }

    /**
     * This method get instance of ShoppingList
     * @param query string of the query
     * @return shopping list
     */
    private ShoppingList getShoppingList(String query) {
        ShoppingList newShoppingListOutput = null;
        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            queryResult.next();
            newShoppingListOutput    = new ShoppingList(queryResult.getInt("id"),
                    queryResult.getString(NAMECOLUMN),
                    queryResult.getInt(USERCOLUMN),
                    queryResult.getString(DATECOLUMN),
                    (queryResult.getInt(ARCHIVECOLUMN) == 1));
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

        return newShoppingListOutput;
    }

    /**
     * Method responsible for retrieving a shopping list by its id
     * @param idShoppingList the id of the shopping list
     * @return a ShoppingList object with all its attributes
     */
    @Override
    public ShoppingList get(int idShoppingList){
        String query = String.format("SELECT * FROM shopping_list WHERE id = %d;", idShoppingList);

        return getShoppingList(query);
    }

    /**
     * Method adding a shopping list
     * @param shoppingList the instance of a shopping list
     * @throws SQLException database exception
     */
    @Override
    public void create(ShoppingList shoppingList) throws SQLException {
        String query = String.format("INSERT INTO shopping_list (name, date, user, archived) VALUES ('%s', '%s', %d," +
                " %d);", shoppingList.getName(), shoppingList.getDate(), shoppingList.getUserID(), shoppingList.getArchived() ? 1 : 0);

        realizeUpdateQuery(query);
    }

    /**
     * Method that updates a shopping list
     * @param shoppingList the instance of a shopping list
     * @throws SQLException database exception
     */
    @Override
    public void update(ShoppingList shoppingList) throws SQLException {
        String query = String.format("UPDATE shopping_list SET name = '%s', date = '%s', user = %d, archived = %d WHERE id = %d",
                shoppingList.getName(), shoppingList.getDate(), shoppingList.getUserID(),
                shoppingList.getArchived() ? 1 : 0, shoppingList.getID()
        );

        this.realizeUpdateQuery(query);
    }

    /**
     * Method that deletes a shopping list
     * @param shoppingList the instance of a shopping list
     * @throws SQLException database exception
     */
    @Override
    public void delete(ShoppingList shoppingList) throws SQLException {
        for (Product product : shoppingList.getProducts()) {
            RecipeListDao.getInstance().delete(new ProductPerList(product.getID(), product.getQuantity(), shoppingList.getID()));
        }
        String query = String.format("DELETE FROM shopping_list WHERE id = %d;", shoppingList.getID());

        this.realizeUpdateQuery(query);
    }

    /**
     * Method extracting all the archived shopping lists of a user
     * @param userId the ID of the user as it stands in the database
     * @return an array containing all the ShoppingList objects constructed with the help of the database
     */
    public List<ShoppingList> getUserArchives(int userId) {
        List<ShoppingList> shoppingLists = new ArrayList<>();
        String query = String.format("SELECT * FROM shopping_list WHERE user = %d AND " +
                "archived = 1", userId);

        return getShoppingLists(shoppingLists, query);
    }
}
