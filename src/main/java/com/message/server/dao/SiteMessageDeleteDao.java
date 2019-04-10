package com.message.server.dao;

import com.message.server.model.SiteMessageDelete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 站内消息删除Dao
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/8/2 15:34
 */
public interface SiteMessageDeleteDao {
    /**
     * 新增删除
     *
     * @param list 消息删除实体列表
     * @return 受影响行数
     */
    int insert(List<SiteMessageDelete> list);

    /**
     * 获取消息删除详情
     *
     * @param messageId 消息id
     * @return
     */
    SiteMessageDelete selectByMessageId(@Param("messageId") String messageId);
}