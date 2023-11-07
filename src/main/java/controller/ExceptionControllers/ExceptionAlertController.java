package controller.ExceptionControllers;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Exception alert Controller class
 */
public class ExceptionAlertController {

    /**
     * Method to show exception window
     * @param throwables To to shown
     */
    public void showExceptionWindow(Exception throwables) {
        Alert alert = alertSetUp();

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        String exceptionMessage = throwables.getMessage();
        printWriter.print(exceptionMessage);
        String exceptionText = stringWriter.toString();
        Label label = new Label("Stacktrace:");
        TextArea textArea = textAreaSetUp(exceptionText);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }

    Alert alertSetUp(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Fatal error:");
        alert.setContentText("An exception has been thrown.");
        return alert;
    }

    TextArea textAreaSetUp(String exceptionText){
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        return textArea;
    }
}
