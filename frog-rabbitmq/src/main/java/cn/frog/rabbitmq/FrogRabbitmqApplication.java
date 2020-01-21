package cn.frog.rabbitmq;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"cn.frog.rabbitmq.dao"})
public class FrogRabbitmqApplication {
    public static void main(String[] args) {
        SpringApplication.run(FrogRabbitmqApplication.class, args);
    }
}
