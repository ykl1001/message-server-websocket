package com.message.server.dao;

import com.message.server.model.SiteMessageAttachment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 站内消息附件表Dao
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/10/9 17:16
 */
public interface SiteMessageAttachmentDao {
    /**
     * 写入附件
     *
     * @param list 附件列表
     * @return
     */
    int insert(List<SiteMessageAttachment> list);

    /**
     * 获取附件列表
     *
     * @param messageId 消息id
     * @return
     */
    List<SiteMessageAttachment> selectAllByMessageId(@Param("messageId") String messageId);
}