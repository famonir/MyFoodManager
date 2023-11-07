package model.FundamentalClasses;

import model.Abstract.NamedTable;
import model.Dao.RecipeDao.RecipeDao;
import model.Dao.FundamentalClasses.UserDao;

import java.util.List;

/**
 * User class (Model)
 */
public class User extends NamedTable {
    private String password;
    private String surname;
    private String username;
    private boolean vegetarian;
    private final String email;
    private final UserDao userDao = UserDao.getInstance();
    private double latitude;
    private double longitude;

    /**
     * Constructor User
     * @param id id of the user
     * @param name name of the user
     * @param passwordToSet password of the user
     * @param surnameToSet surname of the user
     * @param usernameToSet username of the user
     * @param emailToSet email of the user
     * @param vegetarianToSet boolean stating whether the user is vegetarian or not
     * @param latitudeToSet  lat to set
     * @param longitudeToSet lng to set
     */
    public User (
            int id,
            String name,
            String passwordToSet,
            String surnameToSet,
            String usernameToSet,
            String emailToSet,
            boolean vegetarianToSet,
            double latitudeToSet,
            double longitudeToSet
    ) {
        super(id, name);
        this.surname = surnameToSet;
        this.username = usernameToSet;
        this.password = passwordToSet;
        this.vegetarian = vegetarianToSet;
        this.email = emailToSet;
        this.latitude = latitudeToSet;
        this.longitude = longitudeToSet;
    }

    /**
     * Surname setter
     * @param newSurname new surname
     */
    public void setSurname(String newSurname) {
        this.surname = newSurname;
        userDao.update(this);
    }

    /**
     * Surname getter
     * @return surname string
     */
    public String getSurname() {return this.surname;}

    /**
     * Username setter
     * @param newUsername new username
     */
    public void setUsername(String newUsername) {
        this.username = newUsername;
        userDao.update(this);
    }

    /**
     * Password setter
     * @param newPassword New Password String
     */
    public void setPassword(String newPassword){
        this.password = userDao.encrypt(newPassword);
        userDao.update(this);
    }

    /**
     * Vegetarian setter
     * @param isVegetarian Vegetarian Boolean
     */
    public void setVegetarian(Boolean isVegetarian){
        this.vegetarian = isVegetarian;
        userDao.update(this);
    }

    /**
     * Username getter
     * @return username string
     */
    public String getUsername() {return this.username;}

    /**
     * Email getter
     * @return email string
     */
    public String getEmail() {return this.email;}

    /**
     * Vegetarian getter
     * @return vegetarian boolean
     */
    public boolean getVegetarian() {return this.vegetarian;}

    /**
     * Password getter
     * @return password string
     */
    public String getPassword() {return this.password;}

    /**
     * Method to get recipes by user
     * @return list of recipes of the user
     */
    public List<Recipe> getUsersRecipes() {
        return RecipeDao.getInstance().getAllUserRecipes(getID());
    }

    /**
     * Latitude setter
     * @param lat latitude to set
     */
    public void setLatitude(double lat) {
        this.latitude = lat;
        userDao.update(this);
    }

    /**
     * Longitude setter
     * @param lng longitude to set
     */
    public void setLongitude(double lng) {
        this.longitude = lng;
        userDao.update(this);
    }

    /**
     * Latitude getter
     * @return latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Longitude getter
     * @return longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Method to create Recipe
     * @param recipeName name of the recipe
     * @param peopleCount number of people for the recipe
     * @param dishType dish type of the recipe
     */
    public void createRecipe(String recipeName, int peopleCount, String dishType) {
        RecipeDao.getInstance().create(
                new Recipe(
                0, recipeName, getID(), peopleCount, dishType, "", false
                )
        );
    }
}