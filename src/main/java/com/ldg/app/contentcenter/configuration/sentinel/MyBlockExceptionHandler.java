package com.ldg.app.contentcenter.configuration.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 自定义Sentinel异常处理
 *
 * @author ldg
 */
@Slf4j
@Component
public class MyBlockExceptionHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {

        if (e instanceof DegradeException) {
            throw new DegradeException("资源路径降级异常");
        } else if (e instanceof AuthorityException) {
            throw new AuthorityException("授权异常");
        } else if (e instanceof SystemBlockException) {
            throw new SystemBlockException("系统规则异常", "");
        } else if (e instanceof ParamFlowException) {
            throw new ParamFlowException("热点参数异常", "");
        } else if (e instanceof FlowException) {
            throw new FlowException("资源路径被流控了");
        }

    }
}
