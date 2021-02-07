package com.ldg.app.contentcenter.controller;


import com.ldg.app.contentcenter.annotation.CheckLogin;
import com.ldg.app.contentcenter.service.ShareService;
import com.ldg.app.dto.ShareDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ldg
 */
@RestController
@RequestMapping("shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareController {
    private final ShareService shareService;
    /**
     * @param id
     * @param
     * @return
     */
    @CheckLogin
    @GetMapping("{id}")
    public ShareDto findById(@PathVariable Integer id) {
        return shareService.queryDtoById(id);
    }



}
