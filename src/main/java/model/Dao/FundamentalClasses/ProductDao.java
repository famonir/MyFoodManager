package model.Dao.FundamentalClasses;

import controller.ExceptionControllers.ExceptionAlertController;
import model.Dao.Dao;
import model.FundamentalClasses.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Product DAO class
 */
public final class ProductDao extends Dao<Product> {

    private static ProductDao singletonInstance = null;

    private ProductDao() {}
    /**
     * Singleton instance getter
     * @return instance
     */
    public static ProductDao getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new ProductDao();
        }

        return singletonInstance;
    }

    /**
     * Method extracting all products
     * @return a list of Product objects that have a name, a type, a unit and a dose. The quantity is set at 0 as
     * this method is not responsible for retrieving products of a shopping list or a recipe, but products
     * in general.
     */
    @Override
    public List<model.FundamentalClasses.Product> getAll() {
        List<model.FundamentalClasses.Product> allProducts = null;
        String query          = "SELECT * FROM product ORDER BY name;";
        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            allProducts = new ArrayList<>();

            while(queryResult.next()) {
                allProducts.add(new model.FundamentalClasses.Product(
                        queryResult.getInt("id"),
                        queryResult.getString("name"),
                        queryResult.getString("type"),
                        0, queryResult.getString("unit"))
                );
            }
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

        return allProducts;
    }

    /**
     * Method extracting a product based on its name
     * @param nameProduct string representing the name of the product
     * @return a Product object that has a name, a type, a unit and a dose. The quantity is set at 0 as
     * this method is not responsible for retrieving products of a shopping list or a recipe, but products
     * in general.
     */
    @Override
    public model.FundamentalClasses.Product get(String nameProduct) {
        model.FundamentalClasses.Product productOutput = null;
        String query          = String.format("SELECT * FROM product WHERE name = '%s';", nameProduct);
        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            queryResult.next();
            productOutput = new model.FundamentalClasses.Product(
                    queryResult.getInt("id"),
                    queryResult.getString("name"),
                    queryResult.getString("type"), 0,
                    queryResult.getString("unit")
            );
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

        return productOutput;
    }

    /**
     * Delete a product
     * @param product the instance of the product
     */
    public void delete(model.FundamentalClasses.Product product) {
        String query = String.format("DELETE FROM product WHERE name = '%s';", product.getName());

        this.realizeUpdateQuery(query);
    }

    /**
     * Change a product
     * @param product the instance of Product
     */
    public void update(model.FundamentalClasses.Product product) {
        String query = String.format(
                " UPDATE product SET name = '%s', type = '%s', unit = '%s' WHERE id = %d ",
                product.getName(), product.getCategory(), product.getUnits(), product.getID()
        );

        this.realizeUpdateQuery(query);
    }

    /**
     * Method adding a product
     * @param product the instance of the product
     */
    public void create(model.FundamentalClasses.Product product) {
        String query = String.format(" INSERT INTO product (name, type, unit) VALUES ('%s', '%s', '%s'); ",
                product.getName(), product.getCategory(), product.getUnits());

        this.realizeUpdateQuery(query);
    }

    /**
     * @param id id of product
     * @return Product
     * @throws SQLException database exception
     */
    @Override
    public Product get(int id) throws SQLException {
        model.FundamentalClasses.Product productOutput = null;
        String query          = String.format("SELECT * FROM product WHERE id = %d;", id);

        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            queryResult.next();
            productOutput         = new model.FundamentalClasses.Product(
                    queryResult.getInt("id"),
                    queryResult.getString("name"),
                    queryResult.getString("type"), 0,
                    queryResult.getString("unit")
            );
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

        return productOutput;
    }
}
