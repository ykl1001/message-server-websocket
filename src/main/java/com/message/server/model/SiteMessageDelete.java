package com.message.server.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 站内消息删除实体
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/8/2 15:34
 */
@Data
public class SiteMessageDelete implements Serializable {
    private String id;

    private String createUser;

    private String updateUser;

    private Date createTime;

    private Date updateTime;

    private String enterpriseCcode;

    private String deptId;

    private Integer delFlag;

    private String refMessageId;

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

    public String getRefMessageId() {
        return refMessageId;
    }

    public void setRefMessageId(String refMessageId) {
        this.refMessageId = refMessageId;
    }

}