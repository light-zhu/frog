package cn.frog.rabbitmq.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.Timer;

/**
 * <p>Title:MQConnectionTimer</p>
 * <p>Description: 有连接时限的mqConnection</p>
 */
@Slf4j
public class MQConnectionTimer extends MQConnection {

    private long activeTime;
    /*定时器*/
    private Timer timer;

    /*是否定时删除 true:是 false:否*/
    private boolean isTicked;

    public MQConnectionTimer(String host, int port, String username, String password, long activeTime) {
        super(host, port, username, password);
        this.activeTime = activeTime;
        isTicked = false;
    }


    /**
     * 设置超出连接池范围的连接，超时自动删除
     *
     * @param task
     */
    public void tick(MQConnTimerTask task) {
        try {
            if (!isTicked) {
                this.timer = new Timer();
            }
            this.timer.schedule(task, activeTime);

        } catch (Exception ex) {
            log.info("Timer already cancelled.");
        }
    }

    /**
     * 将连接定时关闭删除掉
     */
    public void cancel() {
        if(isTicked) {
            this.timer.cancel();
            isTicked = false;
        }

    }

    public boolean isTicked() {
        return isTicked;
    }

    public void setTicked(boolean ticked) {
        isTicked = ticked;
    }
}
