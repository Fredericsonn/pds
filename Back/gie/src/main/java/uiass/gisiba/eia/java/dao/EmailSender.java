package uiass.gisiba.eia.java.dao;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

    private static String sender = "pds.gie.3@gmail.com";
    private static String password = "htld vcoh tdom faxd";

    public void sendEmail(String receiver, String subject, String emailBody) {
            
        // SMTP server configuration
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
            
        // Create a session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(sender, password);
        }});
        
        // Create a default MimeMessage object
        Message message = new MimeMessage(session);
        // Set From: header field of the header
        try {
            message.setFrom(new InternetAddress(sender));
            // Set To: header field of the header
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
            // Set Subject: header field
            message.setSubject(subject);
            // Now set the actual message
            message.setText(emailBody);
            
            // Send message
            Transport.send(message);
            
            } catch (MessagingException e) {


                e.printStackTrace();
            } 
        

            
        System.out.println("Email sent successfully!");
    }
    

}
