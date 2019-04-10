package com.message.server.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 站内消息实体
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/8/2 15:34
 */
@Data
public class SiteMessage implements Serializable {
    private String id;

    private String createUser;

    private String updateUser;

    private Date createTime;

    private Date updateTime;

    private String enterpriseCcode;

    private String deptId;

    private Integer delFlag;

    private String title;

    private Integer categoryId;

    private String categoryName;

    private String refToUserId;

    private String content;

    private String params;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getEnterpriseCcode() {
        return enterpriseCcode;
    }

    public void setEnterpriseCcode(String enterpriseCcode) {
        this.enterpriseCcode = enterpriseCcode;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getRefToUserId() {
        return refToUserId;
    }

    public void setRefToUserId(String refToUserId) {
        this.refToUserId = refToUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}