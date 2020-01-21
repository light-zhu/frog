package com.frog.mybatisplus.entity;

import lombok.Data;

/**
 * <p>Title:User</p>
 * <p>Description: </p>
 */
@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}