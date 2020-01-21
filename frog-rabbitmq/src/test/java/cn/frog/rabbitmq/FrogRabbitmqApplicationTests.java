package cn.frog.rabbitmq;

import cn.frog.rabbitmq.pool.MQConnectionPool;
import cn.frog.rabbitmq.dao.QueueDao;
import com.rabbitmq.client.Connection;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class FrogRabbitmqApplicationTests {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private QueueDao queueDao;

    @Test
    void contextLoads() {
    }

    @Test
    void testMQPool() throws Exception{
        MQConnectionPool pool = new MQConnectionPool("admin","admin","localhost",5672);
        Connection connection = pool.getConnection();
        Connection connection1 = pool.getConnection();
        Connection connection2 = pool.getConnection();
        Connection connection3 = pool.getConnection();
        Connection connection4 = pool.getConnection();

        Connection connection5 = pool.getConnection();

        pool.releaseConnection(connection);
        System.out.println(pool.getPool().size());
        pool.releaseConnection(connection5);

        Thread.sleep(15000);
        System.out.println(pool.getPool().size());
    }

    @Test
    public void testMysql(){
        String sql = "select 1 from frog_config_queue";
        String string  = jdbcTemplate.queryForObject(sql,String.class);
        System.out.println(string);
    }


    @Test
    public void testMybatis(){
        Integer i = queueDao.countQueue();
        System.out.println(i);
    }

}
