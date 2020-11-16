package org.minibus.aqa.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Error {

    @JsonProperty("success")
    private boolean succeeded;

    @JsonProperty("code")
    private int code;

    @JsonProperty("message")
    private String message;
}
