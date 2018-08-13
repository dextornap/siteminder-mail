package com.siteminder.mail.provider;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequestWithBody;
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
import java.util.stream.Collectors;


@Service("mailGunEmailService")
@ConfigurationProperties
public class MailGunEmailService {

    @NotBlank
    @Value("${config.mailgun.api.key}")
    private String API_KEY;

    @NotBlank
    @Value("${config.mailgun.api.endpoint}")
    private String API_ENDPOINT;

    @Autowired
    Messages messages;

    private static final Logger LOGGER = LoggerFactory.getLogger(MailGunEmailService.class);

    public ApiResponse sendMail(EmailRequest emailRequest) {

        ApiResponse apiResponse;

        try {

            final String FROM = (new StringBuilder()).append("Ranjan Lal <").append(emailRequest.from).append(">").toString();
            final String TO_LIST = emailRequest.to.stream().collect(Collectors.joining(", "));
            final String CC_LIST = emailRequest.cc.get().stream().collect(Collectors.joining(", "));
            final String BCC_LIST = emailRequest.bcc.get().stream().collect(Collectors.joining(", "));

            HttpRequestWithBody executeHttpRequest = Unirest.post(API_ENDPOINT + "/messages")
                    .basicAuth("api", System.getenv(API_KEY))
                    .queryString("from", FROM)
                    .queryString("to", TO_LIST)
                    .queryString("subject", emailRequest.subject.get())
                    .queryString("text", emailRequest.message.get());

            if (!CC_LIST.isEmpty())
                executeHttpRequest = executeHttpRequest.queryString("cc", CC_LIST);

            if (!BCC_LIST.isEmpty())
                executeHttpRequest = executeHttpRequest.queryString("bcc", BCC_LIST);

            HttpResponse<JsonNode> response = executeHttpRequest.asJson();

            apiResponse = new ApiResponse(HttpStatus.resolve(response.getStatus()), response.getBody().toString(), Optional.empty());

        } catch (Exception e) {
            LOGGER.info(messages.get("mailgun.service.failed"));
            apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, messages.get("mailgun.service.failed"), Optional.empty());
        }

        return apiResponse;
    }

}