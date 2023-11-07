package controller.BaseControllers;

import controller.ExceptionControllers.ExceptionAlertController;
import controller.ResourcesAccess.Images;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Abstract class for windows with crosses
 */
public class WithCrossController extends MainAppController {
    private static final int ACTIONBUTTONWITH = 72;
    private static final int HBOXHEIGHT = 30;

    private FileInputStream deleteInput;

    private final String pathToDelete = Images.DELETE.toString();
    private final Image deleteImage;

    /**
     * Constructor setting up the delete image
     */
    protected WithCrossController() {
        try {
            deleteInput = new FileInputStream(pathToDelete);
        } catch (IOException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
        deleteImage = new Image(deleteInput);
    }

    /**
     * Method setting a button
     * @param buttonImgView Image View of the button
     * @return button after the setup
     */
    protected Button setupButton(ImageView buttonImgView) {
        Button button = new Button();
        button.setPrefSize(ACTIONBUTTONWITH, HBOXHEIGHT);
        button.setGraphic(buttonImgView);
        button.setStyle("-fx-background-color : transparent");
        return button;
    }

    /**
     * Method to get action buttons
     * @return int
     */
    public int getActionButtonsWidth() {
        return ACTIONBUTTONWITH;
    }

    /**
     * Method to get hBoxHeight
     * @return int
     */
    public int gethBoxHeight() {
        return HBOXHEIGHT;
    }

    /**
     * Method to get path to delete
     * @return String
     */
    public String getPathToDelete() {
        return pathToDelete;
    }

    /**
     * Method to get delete input
     * @return FileInputStream
     */
    public FileInputStream getDeleteInput() {
        return deleteInput;
    }

    /**
     * Method to delete Image
     * @return Image
     */
    public Image getDeleteImage() {
        return deleteImage;
    }
}
