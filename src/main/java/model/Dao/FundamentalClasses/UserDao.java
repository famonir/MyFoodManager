package model.Dao.FundamentalClasses;

import controller.ExceptionControllers.ExceptionAlertController;
import model.Dao.Dao;
import model.FundamentalClasses.User;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

/**
 * User DAO class
 */
public final class UserDao extends Dao<User> {
    private static final int VALUETOSTRINGBUILDER = 16;
    private static final int MAXHASH = 32;
    private static UserDao singletonInstance = null;

    private UserDao() {}

    /**
     * Singleton instance getter
     * @return instance
     */
    public static UserDao getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new UserDao();
        }

        return singletonInstance;
    }

    /**
     * Mandatory override
     * @return null
     */
    @Override
    public List<User> getAll() {return null;}

    /**
     * User getter from database
     * @param username username to get
     * @return User
     * @throws SQLException database exception
     */
    @Override
    public User get(String username) throws SQLException {
        User user    = null;
        String query = String.format("SELECT * FROM user WHERE username = '%s';", username);

        try (ResultSet queryResult = realizeExecuteQuery(query)) {

            queryResult.next();
            user = new User(
                    queryResult.getInt("id"),
                    queryResult.getString("name"),
                    queryResult.getString("password"),
                    queryResult.getString("surname"),
                    queryResult.getString("username"),
                    queryResult.getString("email"),
                    (queryResult.getInt("vegetarian") == 1),
                    queryResult.getFloat("latitude"),
                    queryResult.getFloat("Longitude")
            );
        } catch (SQLException ignored) {}

        return user;
    }

    /**Mandatory override
     *
     */
    @Override
    public void create(User obj) throws SQLException {}

    /**
     * Method retrieving a user based on an ID
     * @param idUser the id of the user
     * @return a User object with all its instances
     */
    public User get(int idUser) {
        User user    = null;
        String query = String.format("SELECT * FROM user WHERE id = %d;", idUser);

        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            queryResult.next();
            user = new User (
                    idUser,
                    queryResult.getString("name"),
                    queryResult.getString("password"),
                    queryResult.getString("surname"),
                    queryResult.getString("username"),
                    queryResult.getString("email"),
                    (queryResult.getInt("vegetarian") == 1),
                    queryResult.getFloat("latitude"),
                    queryResult.getFloat("Longitude")
            );
        } catch (SQLException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

        return user;
    }

    /**
     * Method retrieving a user based on a name, a surname and a password, it is used to connect a user and
     * to know his/her id
     * @param username the username of the user
     * @param password the password of the user
     * @return a user object set to null if the user does not exist
     */
    public User login(String username, String password) {
        User user = null;
        String query = String.format("SELECT * FROM user WHERE username = '%s' AND password = '%s'",
                username, encrypt(password));

        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            queryResult.next();
            user = new User(
                    queryResult.getInt("id"),
                    queryResult.getString("name"),
                    queryResult.getString("password"),
                    queryResult.getString("surname"),
                    queryResult.getString("username"),
                    queryResult.getString("email"),
                    (queryResult.getInt("vegetarian") == 1),
                    queryResult.getFloat("latitude"),
                    queryResult.getFloat("longitude")
            );
        } catch (SQLException ignored) {}

        return user;
    }


    /**
     * method adding a user
     * @param name name
     * @param password pwd
     * @param surname surname
     * @param username usrname
     * @param email email
     * @param vegetarian vegetarian or not
     * @param latitude lat
     * @param longitude lng
     * @return User
     * @throws SQLException exception regarding sql error
     */
    public User create(String name, String password, String surname, String username, String email, boolean vegetarian, double latitude, double longitude) throws SQLException {
        int intVegetarian        = vegetarian ? 1 : 0;
        String encryptedPassword = this.encrypt(password);
        // Format(Locale.US, ...) used to have doubles separated by . not by ,
        String query             = String.format(Locale.US, " INSERT INTO user (name, password, surname, username, email, vegetarian, latitude, longitude) VALUES" +
                " ( '%s', '%s', '%s', '%s', '%s', %d, '%f', '%f'); ", name,encryptedPassword,surname, username, email, intVegetarian, latitude, longitude);
        this.realizeUpdateQuery(query);

        return this.get(username);
    }

    /**
     * Changes an entry in the user table
     * @param user the instance of the user
     */
    public void update(User user) {
        int intVegetarian        = user.getVegetarian() ? 1 : 0;
        String encryptedPassword = user.getPassword();
        // Format(Locale.US, ...) used to have doubles separated by . not by ,
        String query             = String.format(Locale.US, "UPDATE user SET name = '%s', password = '%s', surname = '%s'," +
                " vegetarian = %d, username = '%s', email = '%s', latitude = '%f', longitude = '%f' WHERE id = %d", user.getName(), encryptedPassword,
                user.getSurname(), intVegetarian, user.getUsername(), user.getEmail(), user.getLatitude(), user.getLongitude(), user.getID());

        this.realizeUpdateQuery(query);
    }

    /**
     * Deletes a user
     * @param user the instance of the user
     */
    public void delete(User user) {
        String query      = String.format(
                "DELETE FROM user WHERE username = '%s'",
                user.getUsername());

        this.realizeUpdateQuery(query);
    }

    /**
     * A method that encrypts a string to it's SHA-1 hex value
     * @param password the string password to encrypt
     * @return a string with the encrypted password
     * code snippet from: <a href="http://www.java2s.com/Tutorial/Java/0490__Security/Encryptapassword.htm">http://www.java2s.com/Tutorial/Java/0490__Security/Encryptapassword.htm</a>
     */
    public String encrypt(String password) {
        String encryptedPass = "";
        try { // Creating a digester (tool transforming strings to encrypted bytes)
            java.security.MessageDigest digester;
            digester = java.security.MessageDigest.getInstance("SHA-1");
            digester.reset();

            // Encrypting the given password
            digester.update(password.getBytes());
            // Building a string builder out of encrypted bytes
            BigInteger no            = new BigInteger(1, digester.digest());
            StringBuilder strBuilder = new StringBuilder(no.toString(VALUETOSTRINGBUILDER));
            while (strBuilder.length() < MAXHASH) {
                strBuilder.insert(0, "0");}
            // Returning the string out of the string builder
            encryptedPass            = strBuilder.toString();
        } catch (NoSuchAlgorithmException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

        return encryptedPass;
    }
}
