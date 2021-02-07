package com.ldg.app.contentcenter.configuration.ribbon;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;
import ribbonconfiguration.RibbonConfiguration;

/**
 * @author ldg
 * 为用户中心服务的Ribbon配置
 * @Configuration
 * @RibbonClient(name = "user-center",configuration = RibbonConfiguration.class) 局部配置
 * @RibbonClients(defaultConfiguration = RibbonConfiguration.class) 全局配置
 */
@Configuration
@RibbonClient(name = "user-center", configuration = RibbonConfiguration.class)
public class UserCenterRibbonConfiguration {
}
