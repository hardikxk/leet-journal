package com.hardik.mailservice.controller;

import com.hardik.mailservice.service.MimeEmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    private final MimeEmailService mimeEmailService;

    public AppController(MimeEmailService mimeEmailService) {
        this.mimeEmailService = mimeEmailService;
    }

    @GetMapping()
    public String hello(){
        return "hello";
    }

    @GetMapping("/mime")
    public ResponseEntity<String> mime(@RequestParam String to, @RequestParam String subject, @RequestParam String text){
        try {
            mimeEmailService.sendEmail(to, subject, text);
            return ResponseEntity.ok("Mail sent successfully using MIME!");
        } catch (MailException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email: " + e.getMessage());
        }
    }
}
