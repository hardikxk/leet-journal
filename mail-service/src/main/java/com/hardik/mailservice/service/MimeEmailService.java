package com.hardik.mailservice.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MimeEmailService {

    private final JavaMailSender mailSender;
    private final String senderEmail;

    public MimeEmailService(JavaMailSender mailSender, @Value("${mail.sender}") String senderEmail) {
        this.mailSender = mailSender;
        this.senderEmail = senderEmail;
    }

    public void sendEmail(String to, String subject, String text) throws MailException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            message.setFrom(senderEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new MailException("Failed to send email", e) {};
        }
    }
}
