package com.ldg.app.contentcenter.configuration.ribbon;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.stream.events.Namespace;

/**
 * @author ldg
 * 实现基于权重的负载均衡
 */
@Slf4j
public class NacosWeightedRule extends AbstractLoadBalancerRule {
    //注入NacosDiscoveryProperties
    @Autowired
    private NacosDiscoveryProperties properties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
        //读取配置文件并初始化
    }

    @Override
    public Server choose(Object o) {
        try {
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
            log.info("lb={}", loadBalancer);
            //想要请求微服务的名称
            String name = loadBalancer.getName();
            log.info("微服务名称:{}", name);
            NamingService namingService = properties.namingServiceInstance();
            log.info("namingService:{}", namingService);
            //从微服务实例中基于权重的选择一个实例
            Instance instance = namingService.selectOneHealthyInstance(name);
            return new NacosServer((instance));
        } catch (NacosException e) {
            e.printStackTrace();
            return null;
        }
    }

}
