package com.siteminder.mail.controller;

import com.siteminder.mail.model.ApiResponse;
import com.siteminder.mail.util.Messages;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
public class EmailControllerIT {

    @Autowired
    Messages messages;

    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
    }

    @Test
    public void getHello() {

        ResponseEntity<ApiResponse> response = template.getForEntity(base.toString(), ApiResponse.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        ApiResponse responseBody = response.getBody();
        assertThat(responseBody.status, equalTo(HttpStatus.OK));
        assertThat(responseBody.message, equalTo(messages.get("welcome.message")));
        assertThat(responseBody.errors.isPresent(), equalTo(false));
    }
}
