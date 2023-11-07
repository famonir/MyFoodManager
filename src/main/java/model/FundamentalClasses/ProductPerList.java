package model.FundamentalClasses;

/**
 * Class used to add a product to a shopping list or to a recipe
 * @param listID id of the list or the recipe
 * @param productID id of the product to change
 * @param quantity quantity of the given product
 */
public record ProductPerList(int productID, int quantity, int listID) {

    /**
     * Product ID getter
     * @return product id
     */
    public int getProductID() {
        return this.productID;
    }

    /**
     * Product quantity getter
     * @return quantity
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * List ID getter
     * @return id of recipe or shopping list
     */
    public int getListID() {
        return this.listID;
    }
}
