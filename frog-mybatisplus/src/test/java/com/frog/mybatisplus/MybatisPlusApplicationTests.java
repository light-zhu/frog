package com.frog.mybatisplus;

import com.alibaba.fastjson.JSON;
import com.frog.mybatisplus.entity.User;
import com.frog.mybatisplus.dao.UserMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MybatisPlusApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void testQueue() {
       /* System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.assertEquals(5, userList.size());
        userList.forEach(System.out::println);*/
        User user = userMapper.getUseById(1);
        System.out.println(JSON.toJSONString(user, true));
    }


}
