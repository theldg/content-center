package com.ldg.app.contentcenter.service;

import com.ldg.app.entity.RocketmqTransactionLog;

import java.util.List;

/**
 * RocketMQ事务日志表(RocketmqTransactionLog)表服务接口
 *
 * @author makejava
 * @since 2021-01-20 18:34:57
 */
public interface RocketmqTransactionLogService {


    /**
     * 查询是否存在和transactionLog相同的数据
     *
     * @param transactionLog
     * @return
     */
    RocketmqTransactionLog selectOne(RocketmqTransactionLog transactionLog);

    /**
     * 新增数据
     *
     * @param rocketmqTransactionLog 实例对象
     * @return 实例对象
     */
    Integer insert(RocketmqTransactionLog rocketmqTransactionLog);


}