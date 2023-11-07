package model.FundamentalClasses;

import model.Dao.FundamentalClasses.ProductDao;
import model.Abstract.NamedTable;

/**
 * Class of product (Model)
 */
public final class Product extends NamedTable {
    private final String category;
    private int quantity;
    private String units;
    private float price;
    private String brand;

    /**
     * Constructor Product with five params
     * @param id id as an int
     * @param name name as a string
     * @param categoryToSet category as a string
     * @param quantityToSet quantity as an int
     * @param unitsToSet units as a string
     */
    public Product(int id, String name, String categoryToSet, int quantityToSet, String unitsToSet) {
        super(id, name);
        this.category = categoryToSet;
        this.quantity = quantityToSet;
        this.units = unitsToSet;
    }

    /**
     * Constructor Product with four params
     * @param name of the product
     * @param categoryToSet category of the product
     * @param quantityToSet quantity of the product
     * @param unitsToSet units of the product
     */
    public Product( String name, String categoryToSet, int quantityToSet, String unitsToSet) {
        super(name);
        this.category = categoryToSet;
        this.quantity = quantityToSet;
        this.units = unitsToSet;
    }

    /**
     * Constructor Product with two params
     * @param name of the product
     * @param quantityToSet of the product
     */
    public Product(String name, int quantityToSet) {
        super(ProductDao.getInstance().get(name).getID(), name);
        Product temp = ProductDao.getInstance().get(name);
        this.category = temp.getCategory();
        this.units = temp.getUnits();
        this.quantity = quantityToSet;
    }

    /**
     * Method checking if two products are the same
     * @param o other Product object
     * @return boolean confirm equality
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return getID() == product.getID() && getName().equals(product.getName());
    }

    @Override
    public int hashCode() {
        return 0;
    }

    /**
     * Method add 1 to quantity
     */
    public void increaseQuantity() {
        ++this.quantity;
    }

    /**
     * Method removing 1 from quantity
     */
    public void decreaseQuantity() {
        if (this.quantity > 1) {
            --this.quantity;
        }
    }

    /**
     * Quantity setter
     * @param newQuantity new quantity
     */
    public void setQuantity(int newQuantity) {this.quantity = newQuantity;}

    /**
     * Quantity getter
     * @return quantity integer
     */
    public int getQuantity() {return this.quantity;}

    /**
     * Category getter
     * @return category string
     */
    public String getCategory() {return this.category;}

    /**
     * Units getter
     * @return units string
     */
    public String getUnits() {return this.units;}

    /** units setter
     * @param unitToSet new units value
     */
    public void setUnits(String unitToSet) {this.units = unitToSet;}

    /** price getter
     * @return price float
     */
    public float getPrice() {return this.price;}

    /**
     * units setter
     * @param priceToSet new price value
     */
    public void setPrice(float priceToSet) { this.price = priceToSet;}

    /** brand getter
     * @return brand string
     */
    public String getBrand() {return this.brand;}

    /**
     * Brand setter
     * @param brandToSet new brand value
     */
    public void setBrand(String brandToSet) { this.brand = brandToSet;}
}



