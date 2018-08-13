package com.siteminder.mail.provider;

import com.sendgrid.*;
import com.siteminder.mail.model.ApiResponse;
import com.siteminder.mail.model.EmailRequest;
import com.siteminder.mail.util.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Service("sendGridEmailService")
@ConfigurationProperties
public class SendGridEmailService {

    @NotBlank
    @Value("${config.sendgrid.api.key}")
    private String API_KEY;

    @NotBlank
    @Value("${config.sendgrid.api.endpoint}")
    private String API_ENDPOINT;

    @Autowired
    Messages messages;

    private static final Logger LOGGER = LoggerFactory.getLogger(SendGridEmailService.class);

    public ApiResponse sendMail(EmailRequest emailRequest) {

        SendGrid sg = new SendGrid(System.getenv(API_KEY));

        Mail mail = new Mail();
        Personalization personalization = new Personalization();

        mail.setFrom(new Email(emailRequest.from));
        emailRequest.to.stream().forEach(to -> personalization.addTo(new Email(to)));
        emailRequest.cc.get().stream().forEach(cc -> personalization.addCc(new Email(cc)));
        emailRequest.bcc.get().stream().forEach(bcc -> personalization.addTo(new Email(bcc)));
        mail.setSubject(emailRequest.subject.get());
        mail.addContent(new Content("text/plain", emailRequest.message.get()));
        mail.addPersonalization(personalization);

        Request request = new Request();
        ApiResponse apiResponse;
        try {
            request.setMethod(Method.POST);
            request.setEndpoint(API_ENDPOINT);
            request.setBody(mail.build());
            Response response = sg.api(request);

            apiResponse = new ApiResponse(HttpStatus.resolve(response.getStatusCode()), response.getBody(), Optional.empty());

        } catch (Exception ex) {
            LOGGER.info(messages.get("sendgrid.service.failed"));
            apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, messages.get("sendgrid.service.failed"), Optional.empty());
        }

        return apiResponse;
    }

}