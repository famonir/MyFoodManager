package model.MailHandler;

import controller.ExceptionControllers.ExceptionAlertController;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * java main util class
 */
public class JavaMailUtil {

    private static final String VALUETRUE = "true";

    /**
     *
     * Method to send email
     * @param recipient recipient of the email
     * @param file file to be sent
     */
    public static void sendMail(String recipient, File file) {
        Properties properties = getProperties();

        String myAccountEmail = "Test.Projetgenie@gmail.com";
        String password = "123TestGenie";

        Session session = Session.getInstance(properties, new Authenticator(){
            @Override
            public PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

        try {
            prepareMessage(session, myAccountEmail, recipient, file);
        } catch (MessagingException | IOException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    /**
     * Getter for the mail properties
     * @return mail properties
     */
    private static Properties getProperties() {
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", VALUETRUE);
        properties.put("mail.smtp.starttls.enable", VALUETRUE);
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        return properties;
    }

    /**
     * Method to prepare message
     * @param session Session object
     * @param myAccountEmail send email account
     * @param recipient recipient email account
     * @param file file to be sent
     * @throws MessagingException messaging exception which can occur when trying to send the mail
     * @throws IOException io exception which can occur when handling the file to be sent
     */
    private static void prepareMessage(
            Session session,
            String myAccountEmail,
            String recipient,
            File file
    ) throws MessagingException, IOException {
            MimeMessage msg = new MimeMessage(session);
            Multipart multipart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            String message = "Here is your shopping list !";
            messageBodyPart.setText(message, "utf-8", "html");
            multipart.addBodyPart(messageBodyPart);

            MimeBodyPart attachement = new MimeBodyPart();
            attachement.attachFile(file);

            multipart.addBodyPart(attachement);
            msg.setContent(multipart);
            msg.setFrom(new InternetAddress(myAccountEmail));
            msg.setRecipient(Message.RecipientType.TO,new InternetAddress(recipient));
            msg.setSubject("Shopping list " + file.getName() + " from myFoodManager");

            Transport.send(msg);
    }

    /** Method sending a password to the user account owner
     * @param recipient recipient of the email
     * @param newPassword the new generated password
     */
    public static void sendPassword(String recipient, String newPassword) {
        Properties properties = getProperties();

        String myAccountEmail = "Test.Projetgenie@gmail.com";
        String password = "123TestGenie";

        Session session = Session.getInstance(properties, new Authenticator(){
            @Override
            public PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

        try {
            prepareEmailPassword(session, myAccountEmail, recipient, newPassword);
        } catch (MessagingException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    private static void prepareEmailPassword (
            Session session,
            String myAccountEmail,
            String recipient,
            String password
    ) throws MessagingException {
            MimeMessage msg = new MimeMessage(session);
            Multipart multipart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            String message = "Here is your new password, change it really quickly : " + password;
            messageBodyPart.setText(message, "utf-8", "html");
            multipart.addBodyPart(messageBodyPart);
            msg.setContent(multipart);
            msg.setFrom(new InternetAddress(myAccountEmail));
            msg.setRecipient(Message.RecipientType.TO,new InternetAddress(recipient));
            msg.setSubject("your new password !");

            Transport.send(msg);
    }
}
