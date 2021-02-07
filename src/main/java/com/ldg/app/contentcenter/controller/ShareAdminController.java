package com.ldg.app.contentcenter.controller;

import com.ldg.app.contentcenter.annotation.CheckAuthorization;
import com.ldg.app.contentcenter.annotation.CheckLogin;
import com.ldg.app.contentcenter.service.ShareService;
import com.ldg.app.dto.ShareAuditDto;
import com.ldg.app.entity.Share;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;


/**
 * @author ldg
 */
@Slf4j
@RestController
@RequestMapping("admin/shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareAdminController {

    private final ShareService shareService;

    @CheckAuthorization("admin")
    @PutMapping("audit/{id}")
    public Share auditById(@PathVariable Integer id, @RequestBody ShareAuditDto auditDto) {
        //TODO 认证,授权

        return shareService.auditById(id, auditDto);
    }
}
