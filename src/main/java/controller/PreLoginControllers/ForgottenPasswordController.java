package controller.PreLoginControllers;

import controller.BaseControllers.WithCrossController;
import javafx.scene.control.*;
import model.Dao.FundamentalClasses.UserDao;
import model.FundamentalClasses.User;
import model.MailHandler.JavaMailUtil;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

/**Controller responsible for the window of recovering a password
 *
 */
public final class ForgottenPasswordController extends WithCrossController {
    private final UserDao userDao = UserDao.getInstance();
    /**Email text field from the fxml file
     *
     */
    @FXML
    private TextField emailTextField;
    /**Return button from the fxml file
     *
     */
    @FXML
    private Button returnButton;
    /**Username text field from the fxml file
     *
     */
    @FXML
    private TextField usernameTextField;

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
     * @throws SQLException exception thrown by database sending issue
     * @throws IOException exception thrown by never existing file
     */
    public void sendMail() throws IOException, SQLException {
        back();
        String emailAdress = emailTextField.getText();
        String username = usernameTextField.getText();
        User user = userDao.get(username);
        String newPassword = randomPassword();
        user.setPassword(newPassword);
        JavaMailUtil.sendPassword(emailAdress, newPassword);
    }

    /**
     * generate a random string. used when the user forget his password
     * @return the string previously generated
     */
    public static String randomPassword() {
        String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int length = 20;
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(alphaNumeric.length());
            char randomChar = alphaNumeric.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }

}
