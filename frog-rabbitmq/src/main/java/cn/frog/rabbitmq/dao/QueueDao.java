package cn.frog.rabbitmq.dao;

import org.apache.ibatis.annotations.Mapper;

/**
 * <p>Title:QueueDao</p>
 * <p>Description: </p>
 */

@Mapper
public interface QueueDao {
    Integer countQueue();
}
