package org.minibus.aqa.domain.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteDTO {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("opDays")
    private List<Integer> opDays;

    @JsonProperty("desc")
    private String desc;

    @JsonProperty("from")
    private CityDTO from;

    @JsonProperty("to")
    private CityDTO to;
}
