package io.marine.modules.generator.model;

import lombok.Data;

@Data
public class InferiorVO {

    private String sea;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;

    private Integer count;
}
