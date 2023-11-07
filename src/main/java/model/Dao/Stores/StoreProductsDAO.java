package model.Dao.Stores;

import controller.ExceptionControllers.ExceptionAlertController;
import model.Dao.Dao;
import model.Dao.FundamentalClasses.ProductDao;
import model.FundamentalClasses.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Dao for the Store Products
 */
public class StoreProductsDAO extends Dao<Product> {
    private final ProductDao productDao = ProductDao.getInstance();
    private static StoreProductsDAO singletonInstance = null;

    private StoreProductsDAO() {}

    /**
     * Singleton instance getter
     * @return instance
     */
    public static StoreProductsDAO getInstance(){
        if (singletonInstance == null) {
            singletonInstance = new StoreProductsDAO();
        }
        return singletonInstance;
    }

    /**
     * Mandatory override
     * @return null
     */
    @Override
    public List<Product> getAll(){ return null;}

    /**
     * Mandatory override
     * @return null
     */
    @Override
    public Product get(int id) throws SQLException {
        return null;
    }

    /**
     * Mandatory override
     * @return null
     */
    @Override
    public Product get(String name) throws SQLException {
        return null;
    }

    /**
     * Method that get all products of a store
     * @param storeName is the name of a store in the database
     * @return a list of Product objects
     */
    public List<Product> getAllProducts(String storeName) {
        List<Product> products = new ArrayList<>();
        int storeId = 0;
        String getIdQuery = String.format("SELECT id FROM stores where name = '%s'", storeName);
        try (ResultSet queryResult = realizeExecuteQuery(getIdQuery)) {
            queryResult.next();
            storeId = queryResult.getInt("id");
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
        if (storeId > 0) {
            String query = String.format("SELECT * FROM storeProducts WHERE storeId = %d;", storeId);

            try (ResultSet queryResult = realizeExecuteQuery(query)) {
                while (queryResult.next()) {
                    Product productAppend = productDao.get(queryResult.getInt("productId"));
                    productAppend.setID(queryResult.getInt("id"));
                    productAppend.setPrice(queryResult.getFloat("price"));
                    productAppend.setUnits(queryResult.getString("unit"));
                    productAppend.setBrand(queryResult.getString("brand"));
                    productAppend.setQuantity(queryResult.getInt("quantity"));
                    products.add(productAppend);
                }
            } catch (SQLException throwables) {
                new ExceptionAlertController().showExceptionWindow(throwables);
            }
        }

            return products;
    }

    /**
     * Mandatory override
     */
    @Override
    public void create(Product obj) {}

    /**
     * Method that update the storeProducts table entries
     * @param product product object
     */
    @Override
    public void update(Product product) {
        String query = String.format(
                Locale.US, "UPDATE storeProducts SET price = %f, brand = '%s', quantity = '%s' WHERE id = %s",
                product.getPrice(), product.getBrand(), product.getQuantity(), product.getID()
        );

        this.realizeUpdateQuery(query);
    }

    /**
     * method that delete a product from a store
     * @param product product object
     */
    @Override
    public void delete(Product product) {
        String query = String.format("DELETE FROM storeProducts where id = %s", product.getID());
        realizeUpdateQuery(query);
    }

    /**
     * Method adding a product to a store
     * @param product the instance of the product that is added
     * @param storeId the id of the store where we add the product
     */
    public void create(Product product, int storeId) {
        String query = String.format(
                Locale.US, "INSERT INTO storeProducts (storeId ,productId, price, unit, brand, quantity) VALUES ( %d, %d, %f, '%s', '%s', %s); ",
                storeId, product.getID(), product.getPrice(), product.getUnits(), product.getBrand(), product.getQuantity()
        );

        this.realizeUpdateQuery(query);
    }

    /**
     * Delete a product from a store
     * @param product the instance of the product
     * @param storeId the id of the store where we add the product
     */
    public void delete(Product product, int storeId) {
        String query = String.format(
                Locale.US, "DELETE FROM storeProducts WHERE storeId = %d AND productId = %d AND price = %f AND brand = '%s'",
                storeId, product.getID(), product.getPrice(), product.getBrand()
        );

        this.realizeUpdateQuery(query);
    }
}
