package org.minibus.aqa.domain.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleDTO {

    @JsonProperty("route")
    private RouteDTO route;

    @JsonProperty("timeline")
    private List<TripDTO> timeline;
}
