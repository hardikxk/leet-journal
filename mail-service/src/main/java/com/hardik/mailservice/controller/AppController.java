package com.hardik.mailservice.controller;

import com.hardik.mailservice.dto.EmailRequest;
import com.hardik.mailservice.service.MimeEmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/mime")
    public String mime(@RequestBody EmailRequest request){
        mimeEmailService.sendEmail(request.to(), request.subject(), request.text());
        return "Mail sent succesfully using MIME!";
    }
}
