package com.hardik.mailservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MimeEmailService {

    private final JavaMailSender mailSender;

    public MimeEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            message.setFrom("hardikkumar0005@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            
            PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS).and(Sanitizers.BLOCKS).and(Sanitizers.STYLES);
            String safeText = policy.sanitize(text);
            
            helper.setText(safeText, true);

            mailSender.send(message);
        }
        catch (MessagingException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
        }
    }
}
