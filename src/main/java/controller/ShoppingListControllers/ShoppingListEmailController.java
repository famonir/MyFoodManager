package controller.ShoppingListControllers;
import com.itextpdf.text.DocumentException;
import controller.ResourcesAccess.Resources;
import controller.BaseControllers.WithCrossController;
import javafx.scene.control.*;
import model.ExceptionMessaging;
import model.Exporter.PDFCreate;
import model.FundamentalClasses.ShoppingList;
import model.MailHandler.JavaMailUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

/**
 * Controller responsible for the email handler of a shopping list
 */
public final class ShoppingListEmailController extends WithCrossController {
    /**Email text field of the fxml file
     *
     */
    @FXML
    private TextField emailTextField;
    /**Return button of the fxml file
     *
     */
    @FXML
    private Button returnButton;

    private ShoppingList shoppingList;

    private File file;


    /**
     * Method creating the PDF file
     * @param sList the shopping list that need to be exported
     * @throws DocumentException exception which could occur  when setupPdf is called
     * @throws IOException exception which could occur  when setupPdf is called
     */
    public void loadShoppingList(ShoppingList sList) throws DocumentException, IOException {
        shoppingList = sList;
        file = new File(shoppingList.getName() + ".pdf");
        PDFCreate exporter = new PDFCreate(shoppingList,file) ;
        exporter.setUpPdf();
    }

    /**
     * Method used to go back the previous window
     * @throws IOException ception thrown by never existing file
     */
    public void back() throws IOException {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Method called when the send e-mail button is clicked
     * @throws IOException thrown by never existing file
     * @throws DocumentException thrown by wrong file input
     * @throws ExceptionMessaging thrown when a file hasn't been deleted
     */
    public void submit() throws IOException, DocumentException, ExceptionMessaging {
        back();
        String emailAdress = emailTextField.getText();
        loadShoppingList(shoppingList);
        JavaMailUtil.sendMail(emailAdress, file);
        if (!file.delete()) {
            throw new ExceptionMessaging("The file " + file.getName() + " has not been deleted.");
        }
    }

    /**
     * Method used to preview the shopping list before sending it
     * @throws IOException exception thrown by never existing file
     */
    public void preview() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Resources.PREVIEWMENU.toString()));
        Parent parent = loader.load();
        PdfPreviewController pdfPreviewController = loader.getController();
        pdfPreviewController.loadUser(user);
        pdfPreviewController.loadShoppingList(shoppingList);

        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Pdf Preview");
        stage.setScene(scene);
        stage.showAndWait();
    }


    /** Getter of the shopping list assigned to the email
     * @return shopping list object
     */
    public ShoppingList getShoppingList() {
        return shoppingList;
    }


    /** Getter for the file
     * @return file
     */
    public File getFile() {
        return file;
    }
}
