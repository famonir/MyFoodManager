package model.Abstract;

import model.FundamentalClasses.Product;

import java.util.List;
import java.util.Objects;

/**
 * Abstract class for all classes containing products
 */
public class ProductContainer extends NamedTable {
    private List<Product> products;
    private final int userID;

    /** Product container constructor
     * @param id id integer
     * @param name string for the name
     * @param userIDToSet user possessing the container
     */
    public ProductContainer(int id, String name, int userIDToSet) {
        super(id, name);
        this.userID = userIDToSet;
    }

    /**
     * Method checking if a product container is vegetarian
     * @return boolean confirming vegetarian container
     */
    public boolean checkVegetarian() {
        boolean vegeterian = true;
        for (model.FundamentalClasses.Product product : products) {
            if (Objects.equals(product.getCategory(), "Meat")) {
                vegeterian = false;
                break;
            }
        }
        return vegeterian;
    }

    /**
     * Method checking if a product container contains fish
     * @return boolean confirming fish presence
     */
    public boolean checkFish() {
        boolean fish = false;
        for (model.FundamentalClasses.Product product : products) {
            if (Objects.equals(product.getCategory(), "Fish")) {
                fish = true;
                break;
            }
        }
        return fish;
    }

    /**
     * User ID getter
     * @return ID of the user
     */
    public int getUserID() {return this.userID;}

    /**
     * Method adding a product to the products List
     * @param product product to be added to the list
     */
    public void addProduct(model.FundamentalClasses.Product product) {this.products.add(product);}

    /**
     * Method deleting a product of the products List
     * @param product product to be deleted
     */
    public void removeProduct(Product product) {this.products.remove(product);}

    /**
     * Products getter
     * @return products
     */
    public List<Product> getProducts() {return this.products;}

    /**
     * Method to set produts
     * @param newProducts the new products list
     */
    public void setProducts(List<Product> newProducts) {
        this.products = newProducts;
    }
}
