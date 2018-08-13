package com.siteminder.mail.service;

import com.siteminder.mail.model.ApiResponse;
import com.siteminder.mail.model.EmailRequest;
import com.siteminder.mail.provider.MailGunEmailService;
import com.siteminder.mail.provider.SendGridEmailService;
import com.siteminder.mail.util.Messages;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private SendGridEmailService sendGridEmailService;

    @Autowired
    private MailGunEmailService mailGunEmailService;

    @Autowired
    Messages messages;

    @Override
    public ApiResponse sendMail(EmailRequest emailRequest) {

        ApiResponse apiResponse = new ApiResponse(HttpStatus.PROCESSING, StringUtils.EMPTY, Optional.empty());

        // Circuit Breaker
        for (int i = 0; !isSuccessResponse(apiResponse) && i < 5; i++) {

            // Attempt to send email via MailGun
            apiResponse = mailGunEmailService.sendMail(emailRequest);

            // Attempt to send email via SendGrid
            if (!isSuccessResponse(apiResponse)) apiResponse = sendGridEmailService.sendMail(emailRequest);

        }

        if (!isSuccessResponse(apiResponse)) apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                messages.get("all.services.failed"), Optional.empty());

        return apiResponse;
    }

    private boolean isSuccessResponse(ApiResponse apiResponse) {
        return apiResponse.status.equals(HttpStatus.OK) || apiResponse.status.equals(HttpStatus.ACCEPTED);
    }

}
