package model.FundamentalClasses;

import model.Abstract.ProductContainer;
import model.Dao.FundamentalClasses.ProductsListDao;
import model.Dao.FundamentalClasses.ShoppingListDao;

import java.sql.SQLException;

/**
 * ShoppingList class
 */
public class ShoppingList extends ProductContainer {
    private String date;
    private boolean archived;
    private final ShoppingListDao shoppingListDao = ShoppingListDao.getInstance();
    private final ProductsListDao productsListDao = ProductsListDao.getInstance();

    /**
     * Constructor ShoppingList
     * @param id id of the shopping list
     * @param name name of the shopping list
     * @param userID user ID of the shopping list
     * @param dateToSet date of the shopping list
     * @param archivedToSet boolean stating whether the shopping list is archived or not
     */
    public ShoppingList(int id, String name, int userID, String dateToSet, boolean archivedToSet) {
        super(id, name, userID);
        this.date = dateToSet;
        this.archived = archivedToSet;
        this.setProducts(productsListDao.getByShoppingList(this.getID()));
    }

    /**
     * Name setter override (+ dB)
     * @param newName new name
     * @throws SQLException database exception
     */
    @Override
    public void setName(String newName) throws SQLException {
        super.setName(newName);
        shoppingListDao.update(this);
    }

    /**
     * Date setter (+ dB)
     * @param newDate new date
     * @throws SQLException database exception
     */
    public void setDate(String newDate) throws SQLException {
        this.date = newDate;
        shoppingListDao.update(this);
    }

    /**
     * Date getter
     * @return date string
     */
    public String getDate() {return this.date;}

    /**
     * Archived setter (+ dB)
     * @param archivedToSet new archived
     * @throws SQLException database exception
     */
    public void setArchived(boolean archivedToSet) throws SQLException {
        this.archived = archivedToSet;
        shoppingListDao.update(this);
    }

    /**
     * Archived getter
     * @return archived boolean
     */
    public boolean getArchived() {return this.archived;}

    /**
     * Product add method override (+ dB)
     * @param product product to be added to the list
     */
    @Override
    public void addProduct(Product product) {
        super.addProduct(product);
        productsListDao.create(new ProductPerList(product.getID(), product.getQuantity(), getID()));
    }

    /**
     * Product remove override (+ dB)
     * @param product product to be deleted
     */
    @Override
    public void removeProduct(model.FundamentalClasses.Product product) {
        super.removeProduct(product);
        productsListDao.delete(new ProductPerList(product.getID(), product.getQuantity(), getID()));
    }
}
