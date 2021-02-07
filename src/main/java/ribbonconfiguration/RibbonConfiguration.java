package ribbonconfiguration;

import com.ldg.app.contentcenter.configuration.ribbon.NacosSameClusterWeightedRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author ldg
 * Ribbon配置类
 * 注意这个配置类要写在springboot启动类扫描不到的地方
 */
@Configuration
public class RibbonConfiguration {
    @Bean
    public IRule ribbonRule() {
        return new NacosSameClusterWeightedRule();
    }
}
