package cn.frog.rabbitmq.pool;

import com.rabbitmq.client.Connection;
import lombok.Data;

import java.util.Vector;

/**
 * <p>Title:MQConnectionPool</p>
 * <p>Description: rabbitmq连接池</p>
 */
@Data
public class MQConnectionPool {

    public static final int INIT_SIZE = 5;
    public static final int MAX_SIZE = 10;

    public static final long activeTime = 5000;

    private String username;
    private String password;
    private String host;
    private int port;

    private Vector<MQConnection> pool = null;

    /**
     * 构造函数
     * @param username
     * @param password
     * @param host
     * @param port
     */
    public MQConnectionPool(String username, String password, String host, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;

        this.initPool();

    }

    /**
     * 初始化连接池
     */
    public void initPool() {
        this.pool = new Vector<>(INIT_SIZE);
        for (int i = 0; i < INIT_SIZE; i++) {
            MQConnection connection = new MQConnection(host, port, username, password);
            this.pool.add(connection);
        }
    }


    /**
     * 该方法用于当连接池数量不够用时，临时生成有生存周期限制的MqConnection
     */
    public MQConnection createNewMQconnectionTimer() {
        synchronized (pool) {
            MQConnectionTimer mqConnectionTimer = new MQConnectionTimer(host, port, username, password, activeTime);
            pool.add(mqConnectionTimer);
            return mqConnectionTimer;
        }

    }

    /**
     * 获取rabbitmq连接
     * @return
     */
    public Connection getConnection() {

        synchronized (pool) {
            MQConnection mqConnection = null;
            Connection connection = null;

            while (true) {
                for (int i = 0; i < pool.size(); i++) {
                    mqConnection = pool.get(i);
                    //当连接空闲
                    if (!mqConnection.isUsed()) {
                        //当连接类型是临时生成的连接
                        if (mqConnection instanceof MQConnectionTimer) {
                            MQConnectionTimer mqConnectionTimer = (MQConnectionTimer) mqConnection;
                            // 取消连接的定时实现
                            mqConnectionTimer.cancel();
                            connection = mqConnectionTimer.getConn();
                            mqConnectionTimer.setIsUsed(true);
                            return connection;
                        } else {
                            connection = mqConnection.getConn();
                            mqConnection.setIsUsed(true);
                            return connection;
                        }
                    }
                }

                //没有空闲连接且连接池数量小于连接池最大数量，创建临时连接
                if (connection == null && pool.size() < MAX_SIZE) {
                    mqConnection = this.createNewMQconnectionTimer();
                    MQConnectionTimer mqConnectionTimer = (MQConnectionTimer) mqConnection;
                    mqConnectionTimer.cancel();
                    connection = mqConnection.getConn();
                    mqConnection.setIsUsed(true);
                    return connection;
                }

                //没有空闲连接且连接池数量小于连接池最大数量，等待连接释放
                if (connection == null && pool.size() == MAX_SIZE) {
                    try {
                        pool.wait();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 释放连接
     * @param conn
     */
    public void releaseConnection(Connection conn) {

        synchronized (pool) {
            for (int i = 0; i < pool.size(); i++) {
                MQConnection mqConnection = pool.get(i);
                //要释放的连接与连接池连接一致
                if (conn == mqConnection.getConn()) {
                    if (mqConnection instanceof MQConnectionTimer) {
                        MQConnectionTimer mqConnectionTimer = (MQConnectionTimer) mqConnection;
                        MQConnTimerTask task = new MQConnTimerTask(pool, mqConnectionTimer);
                        //如果是临时连接，启动定时器，生成周期内未被使用，自动删除
                        mqConnectionTimer.tick(task);
                        mqConnection.setIsUsed(false);
                        pool.notify();
                        break;
                    } else {
                        //固定的连接，一直存在
                        mqConnection.setIsUsed(false);
                        pool.notify();
                        break;
                    }
                }
            }
        }
    }

}

