package com.ldg.app.contentcenter.configuration.sentinel;

import ch.qos.logback.core.joran.conditional.ElseAction;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.UrlCleaner;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Sentinel不支持RESTful API(例如:Sentinel会将shares/1与shares/2分开来处理)
 * 所以需要自己实现
 *
 * @author ldg
 */
@Component
public class MyUrlCleaner implements UrlCleaner {
    //让shares/1与shares/2的返回值一样
    @Override
    public String clean(String s) {
        String[] splits = StringUtils.split(s, "/");
        return Arrays.stream(splits).map(split -> {
            if (NumberUtils.isNumber(split)) {
                return "{numbers}";
            }
            return split;
        }).reduce((a, b) -> a + "/" + b).orElse("");
    }
}
