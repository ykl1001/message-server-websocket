package com.message.server.dao;

import com.message.server.model.SiteMessageRead;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 站内消息阅读记录Dao
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/8/2 15:34
 */
public interface SiteMessageReadDao extends Serializable {
    /**
     * 新增阅读记录
     *
     * @param list 消息阅读记录实体列表
     * @return 受影响行数
     */
    int insert(List<SiteMessageRead> list);

    /**
     * 消息阅读记录
     *
     * @param messageId 消息id
     * @return 消息阅读记录实体
     */
    SiteMessageRead selectByMessageId(@Param("messageId") String messageId);
}