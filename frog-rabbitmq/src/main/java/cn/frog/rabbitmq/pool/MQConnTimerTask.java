package cn.frog.rabbitmq.pool;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.TimerTask;
import java.util.Vector;

/**
 * <p>Title:MQConTimerTask</p>
 * <p>Description: 连接定时失效任务</p>
 */
@Slf4j
public class MQConnTimerTask extends TimerTask {

    private Vector<MQConnection> connectionPool = null;
    private MQConnectionTimer mqConnectionTimer = null;


    public MQConnTimerTask(Vector<MQConnection> pool, MQConnectionTimer connectionTimer) {
        this.connectionPool = pool;
        this.mqConnectionTimer = connectionTimer;
    }

    @Override
    public void run() {

        try {
            this.mqConnectionTimer.getConn().close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            connectionPool.remove(mqConnectionTimer);
            log.info("移除超出生命周期的rabbitmq连接！");
        }

    }
}
