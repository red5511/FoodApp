package com.foodapp.foodapp.user.email;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@AllArgsConstructor
@Slf4j
public class EmailService implements EmailSender {
    public static final String ACTIVATION_LINK_BASE_PATH = "/api/v1/auth/register/confirm/";
    public static final String ACTIVATION_TXT_MSG =
            "Dziękujemy za rejestracje w naszym serwisie. Aby aktywować konto kliknij na podany link: ";
    public static final String PASSWORD_RESET_LINK_BASE_PATH = "/api/v1/auth/password/change/confirm/";
    public static final String PASSWORD_RESET_TXT_MSG =
            "Zarządałeś zmiany hasła. Klikinj na podany link, aby kontynuować: ";

    private final JavaMailSender mailSender;
    private final String domainName;
    private final String domainEmail;

    @Override
    @Async
    public void sendUserActivationEmail(final String to, final String name, final String token) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            String link = domainName + ACTIVATION_LINK_BASE_PATH + token;
            var msg = buildActivationEmail(name, link, ACTIVATION_TXT_MSG, "Aktywuj swoje konto", "Aktywuj");
            helper.setText(msg, true);
            helper.setTo(to);
            helper.setSubject("Aktywuj swoje konto");
            helper.setFrom(domainEmail);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send email");
            throw new IllegalStateException("failed to send email");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    @Async
    public void sendPasswordResetEmail(final String to, final String token) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            String link = domainName + PASSWORD_RESET_LINK_BASE_PATH + token;
            var msg = buildActivationEmail(to, link, PASSWORD_RESET_TXT_MSG, "Zresetuj swoje hasło", "Zresetuj");
            helper.setText(msg, true);
            helper.setTo(to);
            helper.setSubject("Reset your password");
            helper.setFrom(domainEmail);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("failed to send email");
            throw new IllegalStateException("failed to send email");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String buildActivationEmail(final String name, final String link, final String txtMsg, final String title,
                                        final String btnText) {
        return "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Activate Your Account</title>" +
                "    <style>" +
                "        body {" +
                "            font-family: 'Arial', sans-serif;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "            background-color: #f8f9fc;" +
                "            color: #333;" +
                "        }" +
                "        .email-container {" +
                "            max-width: 600px;" +
                "            margin: 30px auto;" +
                "            background-color: #ffffff;" +
                "            border-radius: 10px;" +
                "            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);" +
                "            overflow: hidden;" +
                "            border: 1px solid #ddd;" +
                "        }" +
                "        .header {" +
                "            background-color: #007bff;" +
                "            color: #ffffff;" +
                "            text-align: center;" +
                "            padding: 20px 10px;" +
                "        }" +
                "        .header h1 {" +
                "            margin: 0;" +
                "            font-size: 24px;" +
                "        }" +
                "        .content {" +
                "            padding: 20px;" +
                "            text-align: center;" +
                "        }" +
                "        .content p {" +
                "            font-size: 16px;" +
                "            margin: 15px 0;" +
                "        }" +
                "        .cta-button {" +
                "            display: inline-block;" +
                "            margin-top: 20px;" +
                "            padding: 12px 25px;" +
                "            background-color: #007bff;" +
                "            color: #ffffff;" +
                "            text-decoration: none;" +
                "            font-size: 16px;" +
                "            border-radius: 5px;" +
                "        }" +
                "        .cta-button:hover {" +
                "            background-color: #0056b3;" +
                "        }" +
                "        .footer {" +
                "            padding: 15px 20px;" +
                "            font-size: 14px;" +
                "            color: #666;" +
                "            text-align: center;" +
                "            background-color: #f1f3f5;" +
                "        }" +
                "        .footer p {" +
                "            margin: 0;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"email-container\">" +
                "        <div class=\"header\">" +
                "            <h1>" + title + "</h1>" +
                "        </div>" +
                "        <div class=\"content\">" +
                "            <p>Witaj " + name + ",</p>" +
                "            <p>" + txtMsg + "</p>" +
                "            <a href=\"" + link + "\" class=\"cta-button\">" + btnText + "</a>" +
                "        </div>" +
                "        <div class=\"footer\">" +
//                "            <p>Need help? Contact our support team at <a href=\"mailto:support@yourcompany.com\">support@yourcompany.com</a>.</p>" +
//                "            <p>&copy; 2025 Your Company. All rights reserved.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
}
