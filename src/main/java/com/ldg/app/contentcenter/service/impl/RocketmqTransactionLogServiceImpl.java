package com.ldg.app.contentcenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.ldg.app.contentcenter.mapper.RocketmqTransactionMapper;
import com.ldg.app.contentcenter.service.RocketmqTransactionLogService;
import com.ldg.app.entity.RocketmqTransactionLog;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ldg
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RocketmqTransactionLogServiceImpl implements RocketmqTransactionLogService {
    private final RocketmqTransactionMapper transactionMapper;

    @Override
    public RocketmqTransactionLog selectOne(RocketmqTransactionLog transactionLog) {
        return transactionMapper.selectOne(new Wrapper<RocketmqTransactionLog>() {
            @Override
            public RocketmqTransactionLog getEntity() {
                return transactionLog;
            }

            @Override
            public MergeSegments getExpression() {
                return null;
            }

            @Override
            public void clear() {
            }

            @Override
            public String getSqlSegment() {
                return null;
            }
        });
    }

    @Override
    public Integer insert(RocketmqTransactionLog rocketmqTransactionLog) {
        return transactionMapper.insert(rocketmqTransactionLog);
    }

}
