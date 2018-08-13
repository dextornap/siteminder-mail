package com.siteminder.mail.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.unmodifiableList;
import static org.apache.logging.log4j.util.Strings.EMPTY;

public class EmailRequest {

    private final String EMAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";

    @NotEmpty
    public final @Email(regexp = EMAIL_PATTERN) String from;
    @NotEmpty
    public final List<@Email(regexp = EMAIL_PATTERN) String> to;
    public final Optional<List<@Email(regexp = EMAIL_PATTERN) String>> cc;
    public final Optional<List<@Email(regexp = EMAIL_PATTERN) String>> bcc;
    public final Optional<String> subject;
    public final Optional<String> message;

    @JsonCreator
    public EmailRequest(@JsonProperty("from") String from,
                        @JsonProperty("to") List<String> to,
                        @JsonProperty("cc") Optional<List<String>> cc,
                        @JsonProperty("bcc") Optional<List<String>> bcc,
                        @JsonProperty("subject") Optional<String> subject,
                        @JsonProperty("message") Optional<String> message) {
        this.from = from;
        this.to = to;
        this.cc = Optional.of(cc.orElse(EMPTY_LIST));
        this.bcc = Optional.of(bcc.orElse(EMPTY_LIST));
        this.subject = Optional.of(subject.orElse(EMPTY));
        this.message = Optional.of(message.orElse(EMPTY));
    }

    public List<String> getTo() {
        return unmodifiableList(to);
    }

    public List<String> getCc() {
        return unmodifiableList(cc.isPresent() ? cc.get() : EMPTY_LIST);
    }

    public List<String> getBcc() {
        return unmodifiableList(cc.isPresent() ? bcc.get() : EMPTY_LIST);
    }
}
