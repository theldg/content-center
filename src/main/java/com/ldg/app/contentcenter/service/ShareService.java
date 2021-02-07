package com.ldg.app.contentcenter.service;


import com.ldg.app.dto.ShareAuditDto;
import com.ldg.app.dto.ShareDto;
import com.ldg.app.entity.Share;

/**
 * 分享表(Share)表服务接口
 *
 * @author makejava
 * @since 2021-01-20 18:35:19
 */
public interface ShareService {

    /**
     * 通过ID查询share
     *
     * @param id 主键
     * @return 实例对象
     */
    Share queryById(Integer id);

    /**
     * 通过ID查询sharedto
     *
     * @param id 主键
     * @param
     * @return
     */
    ShareDto queryDtoById(Integer id);


    /**
     * 插入share
     *
     * @param share
     * @return
     */
    Integer insert(Share share);


    /**
     * 审核share
     *
     * @param id
     * @param auditDto
     * @return
     */
    Share auditById(Integer id, ShareAuditDto auditDto);
}