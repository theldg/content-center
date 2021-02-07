package com.ldg.app.contentcenter.service.impl;

import com.ldg.app.contentcenter.feignclient.UserCenterFeignClient;
import com.ldg.app.dto.ShareAuditDto;
import com.ldg.app.dto.ShareDto;
import com.ldg.app.dto.UserAddBonusMsgDto;
import com.ldg.app.entity.Share;
import com.ldg.app.entity.User;
import com.ldg.app.contentcenter.mapper.ShareMapper;
import com.ldg.app.contentcenter.service.ShareService;
import com.ldg.app.enums.AuditStatusEnum;
import com.ldg.app.enums.ReslutCode;
import com.ldg.app.json.JsonAndEntity;
import com.ldg.app.response.ReslutDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.ObjectUtils;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Objects;
import java.util.UUID;

/**
 * @author ldg
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareServiceImpl implements ShareService {

    private final ShareMapper shareMapper;
    private final UserCenterFeignClient feignClient;
    private final Source source;


    @Override
    public Share queryById(Integer id) {

        return shareMapper.selectById(id);
    }

    @Override
    public ShareDto queryDtoById(Integer id) {
        Share share = shareMapper.selectById(id);
        if (Objects.isNull(share)) {
            throw new NullArgumentException("share");
        }
        Integer userId = share.getUserId();
        //怎么通过userId获取用户名称
        //调用用户微服务/users/{userid}
        //1.Nacos
//
//        String url = discoveryClient.getInstances("user-center")
//                   .stream()
//                   .map(instance -> instance.getUri().toString() + "/users/{id}")
//                   .findFirst().orElseThrow(() -> new IllegalArgumentException("当前没有实例对象"));
//        自定义随机负载均衡
//        List<String> urlList = discoveryClient.getInstances("user-center")
//                .stream()
//                .map(instance -> instance.getUri() + "/users/{id}")
//                .collect(Collectors.toList());
//        int random = ThreadLocalRandom.current().nextInt(urlList.size());
//
//        log.info("Rest请求路径:{}",urlList.get(random));
        //2.Resttemplate
        //Ribben实现负载均衡
//        String url = "http://user-center/users/{userId}";
//        ResponseEntity<ReslutDto> forEntity = restTemplate.getForEntity(url, ReslutDto.class, userId);
//        //消息的装配
//        ReslutDto reslutDto = forEntity.getBody();
        //使用Feign

        ReslutDto reslutDto = feignClient.findById(userId);

        User user = JsonAndEntity.toEnity(reslutDto.getData(), User.class);

        if (Objects.isNull(user)) {
            throw new NullArgumentException("user");
        }
        ShareDto shareDto = new ShareDto();
        BeanUtils.copyProperties(share, shareDto);
        shareDto.setWxNickname(user.getWxNickname());
        return shareDto;
    }

    @Override
    public Integer insert(Share share) {
        return shareMapper.insert(share);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Share auditById(Integer id, ShareAuditDto auditDto) {
        //1.查询share是否存在,不存在或者当前的audit_status!=NotYet,那么抛异常
        //2.审核资源,将状态设为Pass/Reject
        //3.如果是Pass,那么为发布人添加积分
        Share share = shareMapper.selectById(id);
        if (Objects.isNull(share)) {
            throw new IllegalArgumentException("参数非法");
        }
        //枚举类型要使用toString()
        if (!ObjectUtils.equals(share.getAuditStatus(), AuditStatusEnum.NotYet.toString())) {
            throw new IllegalArgumentException("资源已审核");
        }
        //更新share
        share = auditInDBById(id, auditDto);
        //如果为Pass,异步执行增加积分
        if (ObjectUtils.equals(share.getAuditStatus(), AuditStatusEnum.Pass.toString())) {
            String transactionId = UUID.randomUUID().toString();
            //发送半消息
            //使用原生RocketMq
//            rocketMQTemplate.sendMessageInTransaction(
//                    "add_bonus",
//                    MessageBuilder.withPayload(
//                            UserAddBonusMsgDto.builder()
//                                    .userId(share.getUserId())
//                                    .bonus(50)
//                                    .build())
//                            //有妙用
//                            .setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
//                            .build(),
//                    null);
            //使用SpringCloudStream
            source.output().send(
                    MessageBuilder.withPayload(
                            UserAddBonusMsgDto.builder()
                                    .userId(share.getUserId())
                                    .bonus(50)
                                    .build())
                            .setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
                            .build());
        }
        return share;
    }

    /**
     * 将审核信息存入数据库
     *
     * @param id
     * @param auditDto
     * @return
     */
    public Share auditInDBById(Integer id, ShareAuditDto auditDto) {
        Share share = Share.builder()
                .id(id)
                .auditStatus(auditDto.getAuditStatusEnum().toString())
                .reason(auditDto.getReason())
                .build();
        shareMapper.updateById(share);
        //可以进行一些其他操作
        return shareMapper.selectById(id);
    }
}
