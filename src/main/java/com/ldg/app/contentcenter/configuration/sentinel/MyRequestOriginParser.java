package com.ldg.app.contentcenter.configuration.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 实现区分消费者来源
 * 不建议把来源放在url中考虑放在header中
 *
 * @author ldg
 * @Component
 */
public class MyRequestOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest request) {
        //从请求参数中获取名为origin的参数并返回如果获取不到origin就抛异常
        String origin = request.getParameter("origin");
        if (StringUtils.isBlank(origin)) {
            throw new IllegalArgumentException("Sentinel中的origin参数必须被指定");
        }
        return origin;
    }

}
