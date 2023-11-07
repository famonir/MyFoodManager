package main;


import controller.ExceptionControllers.ExceptionAlertController;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import controller.ResourcesAccess.Resources;
import controller.ResourcesAccess.Images;

import javax.mail.MessagingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

/**Class of App used to launch the program (+ puts javaFX to classpath)
 *
 */
public class App extends Application {

    /**Method launching the program
     * @param primaryStage stage user to start the program
     * @throws Exception thrown by lack of resources or FXMLLoader
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        checkImages();
        checkResources();
        Parent root = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource(Resources.STARTMENU.toString())));
        primaryStage.setTitle("MyFoodManager");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Method to check images
     */
    public void checkImages() {
        for (Images image : Images.values()) {
            if (!Objects.equals(image.toString(), Images.MAINPATH.toString())) {
                try (FileInputStream fileInputStream = new FileInputStream(image.toString())) {
                    String readInput = fileInputStream.toString();
                    if (readInput == null) {
                        throw new MessagingException("");
                    }
                } catch (IOException | MessagingException throwables) {
                    String errorMessage = image + " not found.\nForce Exit.\n";
                    new ExceptionAlertController().showExceptionWindow(new Exception(errorMessage, throwables));
                    System.exit(1);
                }
            }
        }
    }

    /**Method used by main
     * @param args from main
     */
    public static void app(String[] args) {
        launch(args);
    }

    /**Method checking if all the resources do exist
     */
    public void checkResources() {
        for (Resources resource : Resources.values()) {
            if (!Objects.equals(resource.toString(), Resources.MAINPATH.toString()) &&
                !Objects.equals(resource.toString(), Resources.HTMLPATH.toString()) &&
                !Objects.equals(resource.toString(), Resources.HOMELOCATIONMAPHTML.toString()) &&
                !Objects.equals(resource.toString(), Resources.MAPHTML.toString())) {
                try {
                    FXMLLoader.load(Objects.requireNonNull(getClass().getResource(resource.toString())));
                } catch (IOException throwables) {
                    String errorMessage = resource + " not found.\nForce Exit.\n";
                    new ExceptionAlertController().showExceptionWindow(new Exception(errorMessage, throwables));
                    System.exit(1);
                }
            }
        }
    }
}
