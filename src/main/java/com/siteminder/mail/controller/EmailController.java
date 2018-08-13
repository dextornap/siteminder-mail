package com.siteminder.mail.controller;

import com.siteminder.mail.model.ApiResponse;
import com.siteminder.mail.model.EmailRequest;
import com.siteminder.mail.service.EmailService;
import com.siteminder.mail.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@ConfigurationProperties
public class EmailController {

    @Autowired
    Messages messages;

    @Autowired
    private EmailService emailService;

    @RequestMapping("/")
    public ResponseEntity<ApiResponse> welcome() {
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK, messages.get("welcome.message"), Optional.empty()));
    }

    @PostMapping(path = "/email")
    @ResponseBody
    public ResponseEntity<ApiResponse> sendEmail(@Valid @RequestBody EmailRequest emailRequest) {
        ApiResponse apiResponse = emailService.sendMail(emailRequest);
        ResponseEntity<ApiResponse> responseEntity = new ResponseEntity(apiResponse, apiResponse.status);
        return responseEntity;
    }
}
