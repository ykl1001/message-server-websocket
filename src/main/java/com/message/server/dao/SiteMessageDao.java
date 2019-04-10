package com.message.server.dao;

import com.message.server.model.SiteMessage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 站内消息Dao
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/8/2 15:34
 */
public interface SiteMessageDao extends Serializable {
    /**
     * 新增消息
     *
     * @param record 消息实体
     * @return 受影响行数
     */
    int insert(SiteMessage record);

    /**
     * 获取消息列表
     *
     * @param userId     用户id
     * @param status     阅读状态，1-已阅读，0-未阅读，-1=全部消息
     * @param categoryId 消息分类id，0=全部消息，1-流程消息
     * @return 消息列表
     */
    List<SiteMessage> selectAllByUserId(
            @Param("userId") String userId,
            @Param("status") int status,
            @Param("categoryId") int categoryId);

    /**
     * 获取消息信息
     *
     * @param id     消息id
     * @param userId 用户id
     * @return 消息实体
     */
    SiteMessage selectOneByUserId(
            @Param("id") String id,
            @Param("userId") String userId);

    /**
     * 获取消息数量
     *
     * @param userId     用户id
     * @param status     阅读状态，1-已阅读，0-未阅读，-1=全部消息
     * @param categoryId 消息分类id，0=全部消息，1-流程消息
     * @return
     */
    int selectCount(@Param("userId") String userId,
                    @Param("status") int status,
                    @Param("categoryId") int categoryId);

    /**
     * 获取消息类型类表
     *
     * @return 消息类型列表
     */
    List<Map<String, Object>> selectCategory();
}