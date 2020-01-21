package cn.frog.rabbitmq.pool;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Data;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * <p>Title:MQConnection</p>
 * <p>Description: rabbitmq connection wapper </p>
 */
@Data
public class MQConnection {

    private String host;
    private int port;
    private String username;
    private String password;


    /*是否在使用*/
    private Boolean isUsed;

    private Connection conn;

    public MQConnection(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.isUsed = false;
        this.createConnect();
    }

    /**
     * 创建连接
     */
    public void createConnect() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            factory.setPort(port);
            factory.setUsername(username);
            factory.setPassword(password);
            //连接工厂设置自动恢复连接
            factory.setAutomaticRecoveryEnabled(true);
            conn = factory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }


    public Boolean isUsed() {
        return isUsed;
    }
}
