package com.ashish.fitness.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendEmail(
            String to,
            String username,
            String confirmationUrl,
            String activationCode,
            String subject
    ) throws MessagingException {
        // Build the email content
        String content = buildEmailContent(username, confirmationUrl, activationCode);

        // Create a MIME message
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
        );

        // Set email attributes
        helper.setFrom("onemorerep24x7@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        // Send the email
        mailSender.send(mimeMessage);
    }

    private String buildEmailContent(String username, String confirmationUrl, String activationCode) {
        return "<p>Dear " + username + ",</p>"
                + "<p>Thank you for registering. Please click the link below to activate your account:</p>"
                + "<a href=\"" + confirmationUrl + "?token=" + activationCode + "\">Activate Account</a>"
                + "<p>If you did not request this, please ignore this email.</p>"
                + "<p>Best regards,<br/>Your Company</p>";
    }
}
