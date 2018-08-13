package com.siteminder.mail.service;

import com.siteminder.mail.model.ApiResponse;
import com.siteminder.mail.model.EmailRequest;

public interface EmailService {
    ApiResponse sendMail(EmailRequest emailRequest);
}