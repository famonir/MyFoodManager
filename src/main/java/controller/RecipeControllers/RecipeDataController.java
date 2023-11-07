package controller.RecipeControllers;

import controller.ResourcesAccess.Images;
import controller.BaseControllers.WithCrossController;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Abstract class controlling all the images of recipes contents
 */
public class RecipeDataController extends WithCrossController {
    private final String pathToPeople = Images.PEOPLE.toString();
    private Image favouriteImage;
    private final String pathToFavourite = Images.STAR.toString();
    private Image peopleImage;

    /**
     * Method setting all the images
     */
    public void setImages() {
        try (FileInputStream peopleInput = new FileInputStream(pathToPeople);
             FileInputStream favouriteInput = new FileInputStream(pathToFavourite)) {
            favouriteImage = new Image(favouriteInput);
            peopleImage = new Image(peopleInput);
        } catch (IOException ignored) {}
    }

    /**
     * Method to get people image
     * @return Image
     */
    public Image getPeopleImage() {
        return peopleImage;
    }


    /**
     * Method to get favourite image
     * @return Image
     */
    public Image getFavouriteImage() {
        return favouriteImage;
    }

}
