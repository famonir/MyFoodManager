package model.FundamentalClasses;

import model.Dao.FundamentalClasses.ProductTypeDao;
import java.sql.SQLException;

/**
 * Product type class
 */
public class ProductType {
    private String name;
    private final ProductTypeDao productTypeDao = ProductTypeDao.getInstance();

    /**
     * Constructor ProductType
     * @param nameToSet name of the type of the product
     */
    public ProductType(String nameToSet) {
        this.name = nameToSet;
    }

    /**
     * Name getter
     * @return name of product type
     */
    public String getName() {return this.name;}

    /**
     * Name setter
     * @param newName new name
     * @throws SQLException database exception
     */
    public void setName(String newName) throws SQLException {
        this.name = newName;
        productTypeDao.update(this);
    }
}
