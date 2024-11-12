package org.sunbong.allmart_api.point.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointDTO {
    private Long userID;
    private Integer totalPoints;
}

