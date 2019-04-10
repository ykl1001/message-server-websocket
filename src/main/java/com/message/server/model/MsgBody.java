package com.message.server.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 消息实体
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/8/3 15:42
 */
@Data
public class MsgBody implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户
     */
    public static int RECEIVER_TYPE_USER = 1;

    /**
     * 用户组
     */
    public static int RECEIVER_TYPE_USER_GROUP = 2;

    /**
     * 角色
     */
    public static int RECEIVER_TYPE_ROLE = 3;

    /**
     * 发送人
     */
    private String sendUserId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 消息类型id，100-系统消息，1-流程消息，2-通知公告
     */
    private Integer categoryId;

    /**
     * 消息类型名称
     */
    private String categoryName;

    /**
     * 接收者类型，1-用户，2-用户组，3-角色
     */
    private Integer receiverType;

    /**
     * 接收者
     */
    private String receiver;

    /**
     * 自定义参数；categoryId=1时，params为流程处理连接
     */
    private String params;

    /**
     * 附件
     */
    private List<MsgAttachment> attachment;

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(Integer receiverType) {
        this.receiverType = receiverType;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public List<MsgAttachment> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<MsgAttachment> attachment) {
        this.attachment = attachment;
    }
}
