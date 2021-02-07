package com.ldg.app.contentcenter.configuration.ribbon;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author ldg
 * 相同集群优先调用【基于权重的调用】
 */
@Slf4j
public class NacosSameClusterWeightedRule extends AbstractLoadBalancerRule {
    @Autowired
    private NacosDiscoveryProperties properties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object o) {
        try {
            //1.找到指定服务的所有实例 A
            //2.过滤出相同集群的所有实例 B
            //3.如果B为空,就用A
            //4.基于权重的负载均衡算法，返回一个实例
            //获取集群名称
            String clusterName = properties.getClusterName();
            //Ribbon入口
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
            //获取微服务名称
            String name = loadBalancer.getName();
            NamingService namingService = properties.namingServiceInstance();
            List<Instance> instances = namingService.selectInstances(name, true);
            //根据集群名称进行过滤
            List<Instance> sameList = instances
                    .stream()
                    .filter(instance -> {
                        return Objects.equals(instance.getClusterName(), clusterName);
                    })
                    .collect(Collectors.toList());
            //选择的实例
            Instance hostByRandomWeight = null;
            if (CollectionUtils.isEmpty(sameList)) {
                log.warn("发生了跨集群的调用,name:{} clusterName:{} instances:{}", name, clusterName, instances);
                hostByRandomWeight = ExtendBalancer.getHostByRandomWeight(instances);
            } else {
                log.info("发生相同集群的调用,name:{} clusterName:{}", name, clusterName);
                hostByRandomWeight = ExtendBalancer.getHostByRandomWeight(sameList);
            }
            return new NacosServer(hostByRandomWeight);
        } catch (NacosException e) {
            e.printStackTrace();
            return null;
        }
    }

}
