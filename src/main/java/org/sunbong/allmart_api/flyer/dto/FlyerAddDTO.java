package org.sunbong.allmart_api.flyer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlyerAddDTO {

    private String title;
    private String content;
    private Set<String> audioURL;
    private List<String> attachImages;
}
