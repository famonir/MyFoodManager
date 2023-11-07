package model.JSONImport;

import controller.ExceptionControllers.ExceptionAlertController;
import model.FundamentalClasses.Product;
import model.FundamentalClasses.Recipe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class that imports in the app recipes from JSON files
 */
public class JSONRecipeReader {
    private final int userId;

    /**Constructor of a user's json recipe reader
     * @param newUser user id
     */
    public JSONRecipeReader(int newUser){
        userId =newUser;
    }

    /**
     * Method that extract the data of the recipe from a JSON file
     * @param filename is the name of the JSON file
     * @return a Recipe object containing the data extracted
     */
    public Recipe importRecipeBasicInfos(String filename) {
        try {
            String contents = new String((Files.readAllBytes(Paths.get(filename))));
            JSONObject o = new JSONObject(contents);
            return new Recipe(
                    0,
                    o.getString("name"),
                    userId,
                    o.getInt("peopleAmount"),
                    o.getString("dishType"),
                    o.getString("description"),
                    false
            );
        } catch (IOException | JSONException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

        return null;
    }

    /**
     * Method that extract the data of the recipe's products from a JSON file
     * @param filename is the name of the JSON file
     * @return an Arraylist of Product objects
     */
    public List<Product> importRecipeProducts(String filename) {
        List<Product> toReturn = new ArrayList<>();
        try {
            String contents = new String((Files.readAllBytes(Paths.get(filename))));
            JSONObject o = new JSONObject(contents);
            JSONObject products = o.getJSONObject("products");
            Iterator<?> keys = products.keys();
            while(keys.hasNext()) {
                String key = (String) keys.next();
                toReturn.add(new Product(key,products.getInt(key)));
            }
        } catch (IOException | JSONException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }

        return toReturn;
    }
}
