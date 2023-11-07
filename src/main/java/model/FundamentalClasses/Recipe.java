package model.FundamentalClasses;

import model.Abstract.ProductContainer;
import model.Dao.RecipeDao.RecipeDao;
import model.Dao.RecipeDao.RecipeListDao;
import model.Dao.Planning.RecipesToDayDao;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * Class of Recipe (model)
 */
public class Recipe extends ProductContainer {
    private int peopleCount;
    private String dishType;
    private String description;
    private boolean favourite;
    private final RecipeDao recipeDao = RecipeDao.getInstance();
    private final RecipeListDao recipeListDao = RecipeListDao.getInstance();

    /**
     * constructor recipe
     * @param id id of the recipe
     * @param name name of the recipe
     * @param userID userID of the recipe
     * @param peopleCountToSet number of people of the recipe
     * @param dishTypeToSet dish type of the recipe
     * @param descriptionToSet description of the recipe
     * @param favouriteToSet boolean stating whether the recipe is a favorite of the user or not
     */
    public Recipe(
            int id,
            String name,
            int userID,
            int peopleCountToSet,
            String dishTypeToSet,
            String descriptionToSet,
            boolean favouriteToSet
    ) {
        super(id, name, userID);
        this.peopleCount = peopleCountToSet;
        this.dishType = dishTypeToSet;
        this.description = descriptionToSet;
        this.favourite = favouriteToSet;
        this.setProducts(recipeListDao.getProducts(id));
    }

    /**
     * Name setter override (+ dB)
     * @param newName new name
     * @throws SQLException database exception
     */
    @Override
    public void setName(String newName) throws SQLException {
        super.setName(newName);
        recipeDao.update(this);
    }

    /**
     * Favourite Setter (+ dB)
     * @param newFavourite new favourite
     * @throws SQLException database exception
     */
    public void setFavourite(boolean newFavourite) throws SQLException {
        this.favourite = newFavourite;
        recipeDao.update(this);
    }

    /**
     * Favourite Getter
     * @return favourite boolean
     */
    public boolean getFavourite() {return this.favourite;}

    /**
     * People count setter (+ dB)
     * @param peopleCountToSet new people count
     * @throws SQLException database exception
     */
    public void setPeopleCount(int peopleCountToSet) throws SQLException {
        for (Product product : getProducts()) {
            product.setQuantity(Math.round((float) product.getQuantity() * (float) peopleCountToSet / (float) this.peopleCount));
            recipeListDao.update(new ProductPerList(product.getID(), product.getQuantity(), getID()));
        }
        this.peopleCount = peopleCountToSet;
        recipeDao.update(this);
    }

    /**
     * People count setter (without dB)
     * @param peopleCountToSet new people count
     * @param dayID ID of the day to which the count has to be set
     * @throws SQLException database exception
     */
    public void setPeopleCountNoDBUpdate(int peopleCountToSet, int dayID) throws SQLException {
        for (Product product : getProducts()) {
            product.setQuantity(Math.round((float) product.getQuantity() * (float) peopleCountToSet / (float) this.peopleCount));
        }
        RecipesToDayDao.getInstance().update(new RecipeToDayPlanning(dayID, getID(), peopleCountToSet, peopleCount));
        this.peopleCount = peopleCountToSet;
    }

    /**
     * Method to set people count planning init
     * @param peopleCountToSet number of people of the recipe
     */
    public void setPeopleCountPlanningInit(int peopleCountToSet) {
        for (Product product : getProducts()) {
            product.setQuantity(Math.round((float) product.getQuantity() * (float) peopleCountToSet / (float) this.peopleCount));
        }
        this.peopleCount = peopleCountToSet;
    }

    /**
     * People count getter
     * @return people count integer
     */
    public int getPeopleCount() {return this.peopleCount;}

    /**
     * People count incrementer (+ dB)
     * @throws SQLException database exception
     */
    public void increasePeopleCount() throws SQLException {setPeopleCount(this.peopleCount+1);}

    /**
     * People count decrementer (+ dB)
     * @throws SQLException database exception
     */
    public void decreasePeopleCount() throws SQLException {
        if (this.peopleCount > 1) {
            setPeopleCount(this.peopleCount-1);
        }
    }

    /**
     * Dish type setter (+ dB)
     * @param dishTypeToSet new dish type
     * @throws SQLException database exception
     */
    public void setDishType(String dishTypeToSet) throws SQLException {
        this.dishType = dishTypeToSet;
        recipeDao.update(this);
    }

    /**
     * Dish type getter
     * @return dish type string
     */
    public String getDishType() {return this.dishType;}

    /**
     * Description setter (+ dB)
     * @param descriptionToSet new description
     * @throws SQLException database exception
     */
    public void setDescription(String descriptionToSet) throws SQLException {
        this.description = descriptionToSet;
        recipeDao.update(this);
    }

    /**
     * Description getter
     * @return description string
     */
    public String getDescription() {return this.description;}

    /**
     * Product add method override (+ dB)
     * @param product product to be added to the list
     */
    @Override
    public void addProduct(model.FundamentalClasses.Product product) {
        super.addProduct(product);
        recipeListDao.create(new ProductPerList(product.getID(), product.getQuantity(), getID()));
    }

    /**
     * Product list setter
     * @param productList new products list
     */
    public void setProduct(List<model.FundamentalClasses.Product> productList){
        super.setProducts(productList);
    }

    /**
     * Product remove override (+ dB)
     * @param product product to be deleted
     */
    @Override
    public void removeProduct(model.FundamentalClasses.Product product) {
        super.removeProduct(product);
        recipeListDao.delete(new ProductPerList(product.getID(), product.getQuantity(), getID()));
    }

    /**
     * Equals for AssertEquals
     * @param o object to compare
     * @return boolean stating the result of the comparison
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Recipe recipe = (Recipe) o;

        return  peopleCount == recipe.peopleCount && favourite == recipe.favourite
                && dishType.equals(recipe.dishType) && Objects.equals(description, recipe.description);
    }

    /**
     * This method get hashCode to int
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(peopleCount, dishType, description, favourite);
    }
}
