package com.ldg.app.contentcenter.rocketmq;

import com.ldg.app.contentcenter.service.RocketmqTransactionLogService;
import com.ldg.app.entity.RocketmqTransactionLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 添加积分事务监听
 *
 * @author ldg
 */
@Slf4j
@Service
@RocketMQTransactionListener(txProducerGroup = "tx_add_bonus")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddBonusTransactionListener implements RocketMQLocalTransactionListener {
    private final RocketmqTransactionLogService transactionLogService;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        log.info("MQ执行事务了,message:{}", message);
        MessageHeaders headers = message.getHeaders();
        String transactionId = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);
        try {
            //记录日志
            transactionLogService.insert(RocketmqTransactionLog.builder()
                    .transactionId(transactionId)
                    .log("投稿积分添加")
                    .build());
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    /**
     * 回查确认半消息是否可以发送
     **/
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        MessageHeaders headers = message.getHeaders();
        String transactionId = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);
        //查询一下日志是否存在
        RocketmqTransactionLog transactionLog = transactionLogService.selectOne(
                RocketmqTransactionLog.builder()
                        .transactionId(transactionId)
                        .build()
        );
        log.info("transactionLog:{}", transactionLog);
        if (Objects.isNull(transactionLog)) {
            return RocketMQLocalTransactionState.ROLLBACK;
        } else {
            return RocketMQLocalTransactionState.COMMIT;
        }
    }
}
