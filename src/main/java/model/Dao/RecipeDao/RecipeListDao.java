package model.Dao.RecipeDao;

import controller.ExceptionControllers.ExceptionAlertController;
import model.Dao.Dao;
import model.Dao.FundamentalClasses.ProductDao;
import model.FundamentalClasses.Product;
import model.FundamentalClasses.ProductPerList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Recipe list DAO class
 */
public final class RecipeListDao extends Dao<ProductPerList> {
    private final ProductDao productDao = ProductDao.getInstance();
    private static RecipeListDao singletonInstance = null;

    private RecipeListDao() {}

    /**
     * Singleton instance getter
     * @return instance
     */
    public static RecipeListDao getInstance()
    {
        if (singletonInstance == null) {
            singletonInstance = new RecipeListDao();
        }

        return singletonInstance;
    }

    /**
     * Mandatory override
     * @return null
     */
    @Override
    public List<ProductPerList> getAll() {
        return null;
    }

    /**
     * Mandatory override
     * @return null
     */
    @Override
    public ProductPerList get(int id) throws SQLException {
        return null;
    }

    /**
     * Mandatory override
     * @return null
     */
    @Override
    public ProductPerList get(String name) throws SQLException {
        return null;
    }

    /**
     * Method retrieving all the products of a recipe. Used by Recipe constructor
     * @param recipeID the id of the recipe
     * @return an array of Product objects
     */
    public List<Product> getProducts(int recipeID) {
        List<Product> products = new ArrayList<>();
        String query                = String.format("SELECT * FROM recipe_list WHERE recipe = %d;", recipeID);

        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            while(queryResult.next()) {
                Product productAppend = productDao.get(queryResult.getInt("product"));
                productAppend.setQuantity(queryResult.getInt("quantity")); // nécessaire ??
                products.add(productAppend);
            }
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

        return products;
    }

    /**
     * Method adding a product to a recipe
     * @param recipeList the instance of the product list that is added
     */
    public void create(ProductPerList recipeList) {
        String query = String.format(
                Locale.US, "INSERT INTO recipe_list (product, quantity, recipe) VALUES ( %d, %d, %d); ",
                recipeList.getProductID(), recipeList.getQuantity(), recipeList.getListID()
        );

        this.realizeUpdateQuery(query);
    }

    /**
     * Change a product quantity in a recipe
     * @param recipeList the instance of the product list
     */
    public void update(ProductPerList recipeList) {
        String query = String.format(
                Locale.US, "UPDATE recipe_list SET quantity = %d WHERE recipe = %d AND product = %d",
                recipeList.getQuantity(), recipeList.getListID(), recipeList.getProductID()
        );

        this.realizeUpdateQuery(query);
    }

    /**
     * Delete a recipe's product
     * @param recipeList the instance of the product list
     */
    public void delete(ProductPerList recipeList) {
        String query = String.format(
                "DELETE FROM recipe_list WHERE product = '%s' AND recipe = '%s'",
                recipeList.getProductID(), recipeList.getListID()
        );

        this.realizeUpdateQuery(query);
    }
}
