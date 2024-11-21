package org.sunbong.allmart_api.flyer.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.sunbong.allmart_api.common.domain.BaseEntity;
import org.sunbong.allmart_api.product.domain.ProductImage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tbl_flyer")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(callSuper = true, exclude = {"attachFiles", "audioURL"})
public class Flyer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flyerID;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tbl_flyer_audio")
    @Builder.Default
    @BatchSize(size = 20)
    private Set<String> audioURL = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "tbl_flyer_image")
    @BatchSize(size = 50)
    @Builder.Default
    private List<FlyerImage> attachFiles = new ArrayList<>();

}
