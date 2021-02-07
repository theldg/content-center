package com.ldg.app.contentcenter.configuration.ribbon;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;

import java.util.List;

/**
 * @author ldg
 * 基于权重的负载均衡算法
 */
public class ExtendBalancer extends Balancer {

    public static Instance getHostByRandomWeight(List<Instance> hosts) {
        return Balancer.getHostByRandomWeight(hosts);
    }
}
