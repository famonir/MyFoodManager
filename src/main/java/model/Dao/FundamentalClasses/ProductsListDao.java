package model.Dao.FundamentalClasses;

import controller.ExceptionControllers.ExceptionAlertController;
import model.Dao.Dao;
import model.FundamentalClasses.Product;
import model.FundamentalClasses.ProductPerList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Product list DAO class
 */
public final class ProductsListDao extends Dao<ProductPerList> {
    private final ProductDao productDao = ProductDao.getInstance();
    private static ProductsListDao singletonInstance = null;

    private ProductsListDao() {}

    /**
     * Singleton instance getter
     * @return instance
     */
    public static ProductsListDao getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new ProductsListDao();
        }

        return singletonInstance;
    }

    /**
     * Method that retrieves all the products of a shopping list. This method is used by ShoppingList constructor
     * @param idShoppingList the id of the shopping list
     * @return an array of Product objects with the help of the getProduct method. Only here, the quantities
     * are not set to 0, but to the shopping list values
     */
    public List<Product> getByShoppingList(int idShoppingList) {
        List<Product> products = new ArrayList<>();
        String query                = String.format("SELECT * FROM products_list WHERE list = %d;", idShoppingList);

        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            while(queryResult.next()) {
                Product productAppend = productDao.get(queryResult.getInt("product"));
                productAppend.setQuantity(queryResult.getInt("quantity"));
                products.add(productAppend);
            }
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

        return products;
    }

    /**
     * Method adding a product to a shopping list
     * @param productList the instance of the productList
     */
    @Override
    public void create(ProductPerList productList) {
        String query = String.format(
                "INSERT INTO products_list (list, product, quantity) VALUES ( %d, %d, %d); ",
                productList.getListID(), productList.getProductID(), productList.getQuantity()
        );

        this.realizeUpdateQuery(query);
    }

    /**
     * Change a product quantity in a shopping list
     * @param productList the instance of the productList
     */
    @Override
    public void update(ProductPerList productList) {
        String query = String.format (
                Locale.US,"UPDATE products_list SET quantity = %d WHERE product = %d AND list = %d ",
                productList.getQuantity(), productList.getProductID(), productList.getListID()
        );

        this.realizeUpdateQuery(query);
    }

    /**
     * Delete a shopping list's product
     * @param productList the productList
     */
    @Override
    public void delete(ProductPerList productList) {
        String query = String.format(
                Locale.US,"DELETE FROM products_list WHERE list = %d AND product = %d AND quantity = %d ",
                productList.getListID(), productList.getProductID(), productList.getQuantity()
        );

        this.realizeUpdateQuery(query);
    }

    /**
     * Unused override
     * @return unused overrided method
     */
    @Override
    public List<ProductPerList> getAll() {
        return null;
    }

    /**
     * Unused getter
     * @param id id of product_list or recipe_list
     * @return unused method
     * @throws SQLException database exception
     */
    @Override
    public ProductPerList get(int id) throws SQLException {
        return null;
    }

    /**
     * Unused getter
     * @param name id of product_list or recipe_list
     * @return unused method
     * @throws SQLException database exception
     */
    @Override
    public ProductPerList get(String name) throws SQLException{
        return null;
    }
}
