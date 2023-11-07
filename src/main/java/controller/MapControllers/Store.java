package controller.MapControllers;

import model.FundamentalClasses.Product;

import java.util.List;

/**
 * Store Controller
 */
public class Store {
    private final double longitude;
    private final double latitude;
    private final String storeName;
    private float storePriceForSL = 0.f;
    private List<Product> productsForSL = null;

    /**
     * Constructor, sets global vars
     * @param newLongitude new Longitude
     * @param newLatitude new Latitude
     * @param newStoreName new Store Name
     */
    public Store(double newLongitude, double newLatitude, String newStoreName) {
        this.longitude=newLongitude;
        this.latitude=newLatitude;
        this.storeName=newStoreName;
    }

    /**
     * Method to get products for sl
     * @return Product List
     */
    public List<Product> getProductsForSL() {return this.productsForSL;}

    /**
     * Method to set products for sl
     * @param products list of Product objects
     */
    public void setProductsForSL(List<Product> products) {this.productsForSL = products;}

    /**
     * Getter for the latitude
     * @return store latitude coordinate
     */
    public double getLat() {return this.latitude;}

    /**
     * Getter for longitude of the store
     * @return store longitude coordinate
     */
    public double getLng() {return this.longitude;}

    /**
     * Getter for the name of the store
     * @return store name
     */
    public String getStoreName() {return this.storeName;}

    /**
     * Setter store price for sl
     * @param newPrice new price of a product
     */
    public void setstorePriceForSL(float newPrice) {this.storePriceForSL=newPrice;}

    /**
     * Getter store price for sl
     * @return price of the product on this store
     */
    public float getStorePriceForSL() {return this.storePriceForSL;}
}
