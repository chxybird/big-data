package com.bird.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author 李璞
 * @Date 2022/1/18 13:16
 * @Description
 */
@Data
@Accessors(chain = true)
public class CarDTO {
    /**
     * id
     */
    private String id;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 速度
     */
    private String speed;

    /**
     * 是否贴罚单
     */
    private Boolean isPunish;

}
