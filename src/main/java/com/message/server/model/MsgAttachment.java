package com.message.server.model;

import lombok.Data;

/**
 * 消息附件
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/10/9 17:16
 */
@Data
public class MsgAttachment {
    /**
     * 附件id
     */
    private String fileId;

    /**
     * 附件名称
     */
    private String fileName;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
