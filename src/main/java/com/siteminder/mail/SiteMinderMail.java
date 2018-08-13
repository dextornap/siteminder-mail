package com.siteminder.mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class SiteMinderMail {
    public static void main(String[] args) {
        SpringApplication.run(SiteMinderMail.class, args);
    }
}
