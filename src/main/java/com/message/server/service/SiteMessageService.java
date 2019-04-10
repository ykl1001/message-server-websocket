package com.message.server.service;

import com.message.server.model.MsgBody;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 站内消息
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/8/2 15:34
 */
public interface SiteMessageService extends Serializable {
    /**
     * 新增消息
     *
     * @param msgBody 消息体
     * @return
     */
    void insert(MsgBody msgBody);

    /**
     * 获取消息详情
     *
     * @param id   消息id
     * @param userId 用户id
     * @return
     */
    Map<String, Object> selectOneByUserId(String id, String userId);

    /**
     * 获取用户消息类表
     *
     * @param userId     用户id
     * @param status     消息状态， 1-已阅读，0-未阅读，-1=全部消息
     * @param categoryId 消息分类id，0=全部消息，1-流程消息
     * @param pageNum    当前页
     * @param pageSize   每页显示数据条数
     * @return
     */
    Map<String, Object> selectPage(String userId, int status, int categoryId, int pageNum, int pageSize);

    /**
     * 获取新消息数量
     *
     * @param userId 用户id
     * @return
     */
    int selectNewCount(String userId);

    /**
     * 获取新消息总数量
     *
     * @param userId 用户id
     * @return
     */
    List<Map> selectTotalCount(String userId);

    /**
     * 删除消息
     *
     * @param userId 用户Id
     * @param ids  消息id
     * @return
     */
    int deleteMessage(String userId, String... ids);

    /**
     * 标记已读
     *
     * @param userId   用户信息
     * @param idList 消息id列表
     * @return
     */
    int signRead(String userId, List<String> idList);
}
