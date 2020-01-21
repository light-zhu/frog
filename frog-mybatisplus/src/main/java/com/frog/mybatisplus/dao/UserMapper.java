package com.frog.mybatisplus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.frog.mybatisplus.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * <p>Title:UserMapper</p>
 * <p>Description: </p>
 */
public interface UserMapper extends BaseMapper<User> {

    User getUseById(@Param("id") Integer id);
}
