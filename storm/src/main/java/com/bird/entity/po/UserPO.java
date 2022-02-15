package com.bird.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 李璞
 * @Date 2022/2/14 15:08
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPO {
    private Long id;
    private String name;
    private Integer age;
    private String sex;
    private Integer score;
}
