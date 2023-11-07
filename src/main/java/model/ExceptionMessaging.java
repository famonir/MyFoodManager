package model;

import javax.mail.MessagingException;

/**Class creating a messaging exception
 *
 */
public class ExceptionMessaging extends MessagingException {
    /**Constructor with a message
     * @param message message of the exception
     */
    public ExceptionMessaging(String message) {
        super(message);
    }

    /**Constructor with a message and an exception
     * @param message message of the exception
     * @param e exception thrown
     */
    public ExceptionMessaging(String message, Exception e) {
        super(message, e);
    }
}
