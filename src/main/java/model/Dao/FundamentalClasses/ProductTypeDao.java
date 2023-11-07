package model.Dao.FundamentalClasses;

import model.Dao.Dao;
import model.FundamentalClasses.ProductType;
import java.sql.SQLException;
import java.util.List;

/**
 * Product type DAO class
 */
public final class ProductTypeDao extends Dao<ProductType> {

    private static ProductTypeDao singletonInstance = null;

    private ProductTypeDao() {}
    /**
     * Singleton instance getter
     * @return instance
     */
    public static ProductTypeDao getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new ProductTypeDao();
        }

        return singletonInstance;
    }

    /**
     * Method adding a product category
     * @param productType the name of the category
     */
    public void create(ProductType productType) {
        String query = String.format(" INSERT INTO product_types (name) VALUES ('%s')", productType.getName());
        this.realizeUpdateQuery(query);
    }

    /**
     * Change a product type
     * @param productTypeName the new type name
     * @param oldProductTypeName the old type name since it is a primary key
     */
    public void update(String productTypeName, String oldProductTypeName) {
        String query = String.format(
                "UPDATE product_types SET name = '%s' WHERE name = '%s'",
                productTypeName, oldProductTypeName
        );

        this.realizeUpdateQuery(query);
    }

    /**
     * Delete a product category
     * @param productType the category's
     */
    @Override
    public void delete(ProductType productType) {
        String query = String.format("DELETE FROM product_types WHERE name = '%s'", productType.getName());
        this.realizeUpdateQuery(query);
    }

    /**
     * Mandatory override
     * @return null
     */
    @Override
    public List<ProductType> getAll() {return null;}

    /**
     * Mandatory override
     * @return null
     */
    @Override
    public ProductType get(int id) throws SQLException {return null;}

    /**
     * Mandatory override
     * @return null
     */
    @Override
    public ProductType get(String name) throws SQLException {return null;}

    /**
     * Mandatory override
     */
    @Override
    public void update(ProductType obj) throws SQLException {}
}
