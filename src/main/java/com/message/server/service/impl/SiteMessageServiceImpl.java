package com.message.server.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.message.server.core.enums.MsgTypeEnum;
import com.message.server.core.util.DateUtil;
import com.message.server.core.util.GlobalUserUtil;
import com.message.server.core.util.ObjectUtil;
import com.message.server.core.util.StringUtil;
import com.message.server.dao.*;
import com.message.server.model.*;
import com.message.server.service.SiteMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 站内消息
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/8/2 15:35
 */
@Service
public class SiteMessageServiceImpl implements SiteMessageService {
    private static final long serialVersionUID = 1L;

    /**
     * 全部消息
     */
    private static final int MESSAGE_STATUS_ALL = -1;

    /**
     * 已读消息
     */
    private static final int MESSAGE_STATUS_READ = 1;

    /**
     * 未读消息
     */
    private static final int MESSAGE_STATUS_UN_READ = 0;

    @Autowired
    private SiteMessageDao siteMessageDao;

    @Autowired
    private SiteMessageReadDao siteMessageReadDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SiteMessageDeleteDao siteMessageDeleteDao;

    @Autowired
    private SiteMessageAttachmentDao siteMessageAttachmentDao;

    @Override
    public void insert(MsgBody msgBody) {
        SiteMessage message = new SiteMessage();
        message.setCreateUser(msgBody.getSendUserId());
        message.setCreateTime(DateUtil.getNowDate());
        message.setDelFlag(0);
        message.setTitle(msgBody.getTitle());
        message.setCategoryId(msgBody.getCategoryId());
        message.setCategoryName(msgBody.getCategoryName());
        message.setParams(msgBody.getParams());

        if (msgBody.getReceiverType() == MsgBody.RECEIVER_TYPE_USER) {
            message.setId(StringUtil.getUUID());
            // 用户
            message.setRefToUserId(msgBody.getReceiver());
            // 消息内容
//            String greet = userDao.selectUserNameById(msgBody.getReceiver()) + "&nbsp;您好！<br/> &nbsp;&nbsp;&nbsp;&nbsp;";
            String greet = msgBody.getReceiver() + "&nbsp;您好！<br/> &nbsp;&nbsp;&nbsp;&nbsp;";
            message.setContent(greet + msgBody.getContent());

            int count = siteMessageDao.insert(message);
            // 给websocket用户发送消息
            if (count > 0) {
                // 保存附件
                this.saveAttachment(msgBody, message.getId());

                GlobalUserUtil.sendMessageByUserId(message, MsgTypeEnum.TODO);
            }
        } else {
            List<Map<String, String>> userIdList = new ArrayList<>();
            if (msgBody.getReceiverType() == MsgBody.RECEIVER_TYPE_USER_GROUP) {
                // 用户组
                userIdList = userDao.selectByGroupCode(msgBody.getReceiver());
            } else if (msgBody.getReceiverType() == MsgBody.RECEIVER_TYPE_ROLE) {
                // 角色
                userIdList = userDao.selectByRoleCode(msgBody.getReceiver());
            }

            for (Map<String, String> map : userIdList) {
                message.setId(StringUtil.getUUID());
                message.setRefToUserId(map.get("id"));
                // 消息内容
//                String greet = userDao.selectUserNameById(map.get("id")) + "您好！\n";
                String greet = map.get("id") + "您好！\n";
                message.setContent(greet + msgBody.getContent());
                int count = siteMessageDao.insert(message);

                // 给websocket用户发送消息
                if (count > 0) {
                    // 保存附件
                    this.saveAttachment(msgBody, message.getId());

                    GlobalUserUtil.sendMessageByUserId(message, MsgTypeEnum.TODO);
                }
            }
        }
    }

    @Override
    public Map<String, Object> selectOneByUserId(String id, String userId) {
        SiteMessage message = siteMessageDao.selectOneByUserId(id, userId);

        Map<String, Object> messageMap = ObjectUtil.objectToMap(message);

        // 获取附件
        messageMap.put("attachmentList", this.getAttachmentList(message.getId()));

        if (siteMessageReadDao.selectByMessageId(id) == null) {

            // 添加消息已读日志
            SiteMessageRead messageRead = new SiteMessageRead();
            messageRead.setId(StringUtil.getUUID());
            messageRead.setCreateUser(userId);
            messageRead.setCreateTime(DateUtil.getNowDate());
            messageRead.setEnterpriseCcode("");
            messageRead.setDeptId("");
            messageRead.setDelFlag(0);
            messageRead.setRefMessageId(message.getId());

            List<SiteMessageRead> list = new ArrayList<>(1);
            list.add(messageRead);
            siteMessageReadDao.insert(list);
        }

        return messageMap;
    }

    @Override
    public Map<String, Object> selectPage(String userId, int status, int categoryId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize, "create_time desc");
        List<SiteMessage> list = siteMessageDao.selectAllByUserId(userId, status, categoryId);
        PageInfo<SiteMessage> pageInfo = new PageInfo<>(list);

        List<Map> mapList = new ArrayList<>();
        Map<String, Object> map;
        for (SiteMessage message : pageInfo.getList()) {
            map = new HashMap<>(5);
            map.put("id", message.getId());
            map.put("createTime", message.getCreateTime());
            map.put("title", message.getTitle());
            map.put("categoryName", message.getCategoryName());
            if (status != MESSAGE_STATUS_ALL) {
                map.put("status", status);
            } else {
                if (siteMessageReadDao.selectByMessageId(message.getId()) == null) {
                    map.put("status", MESSAGE_STATUS_UN_READ);
                } else {
                    map.put("status", MESSAGE_STATUS_READ);
                }
            }

            mapList.add(map);
        }

        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("total", pageInfo.getTotal());
        resultMap.put("data", mapList);

        return resultMap;
    }

    @Override
    public int selectNewCount(String userId) {
        return siteMessageDao.selectCount(userId, 0, 0);
    }

    @Override
    public List<Map> selectTotalCount(String userId) {
        List<Map<String, Object>> list = siteMessageDao.selectCategory();

        List<Map> mapList = new ArrayList<>();
        Map<String, Object> map;
        for (Map<String, Object> item : list) {
            map = new HashMap<>(4);
            int categoryId = (Integer) item.get("category_id");

            map.put("id", categoryId);

            // 消息总条数
            map.put("totalCount", siteMessageDao.selectCount(userId, -1, categoryId));
            // 未读消息
            map.put("unReadCount", siteMessageDao.selectCount(userId, 0, categoryId));
            // 已读消息
            map.put("readCount", siteMessageDao.selectCount(userId, 1, categoryId));

            mapList.add(map);
        }

        return mapList;
    }

    @Override
    public int deleteMessage(String userId, String... ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }

        List<String> newIds = new ArrayList<>();
        for (String id : ids) {
            if (siteMessageDeleteDao.selectByMessageId(id) == null
                    && siteMessageDao.selectOneByUserId(id, userId) != null) {
                newIds.add(id);
            }
        }

        if (newIds.isEmpty()) {
            return 0;
        }

        List<SiteMessageDelete> messageDeleteList = new ArrayList<>(newIds.size());
        SiteMessageDelete messageDelete;
        for (String id : newIds) {
            messageDelete = new SiteMessageDelete();
            messageDelete.setId(StringUtil.getUUID());
            messageDelete.setCreateUser(userId);
            messageDelete.setCreateTime(DateUtil.getNowDate());
            messageDelete.setEnterpriseCcode("");
            messageDelete.setDeptId("");
            messageDelete.setDelFlag(0);
            messageDelete.setRefMessageId(id);
            messageDeleteList.add(messageDelete);
        }

        return siteMessageDeleteDao.insert(messageDeleteList);
    }

    @Override
    public int signRead(String userId, List<String> idList) {
        if (idList == null || idList.size() == 0) {
            return 0;
        }

        List<String> newIds = new ArrayList<>();
        for (String id : idList) {
            if (siteMessageReadDao.selectByMessageId(id) == null
                    && siteMessageDao.selectOneByUserId(id, userId) != null) {
                newIds.add(id);
            }
        }

        if (newIds.isEmpty()) {
            return 0;
        }

        List<SiteMessageRead> messageReadList = new ArrayList<>(newIds.size());
        SiteMessageRead messageRead;
        for (String id : newIds) {
            messageRead = new SiteMessageRead();
            messageRead.setId(StringUtil.getUUID());
            messageRead.setCreateUser(userId);
            messageRead.setCreateTime(DateUtil.getNowDate());
            messageRead.setEnterpriseCcode("");
            messageRead.setDeptId("");
            messageRead.setDelFlag(0);
            messageRead.setRefMessageId(id);
            messageReadList.add(messageRead);
        }

        return siteMessageReadDao.insert(messageReadList);
    }

    /**
     * 保存消息附件
     *
     * @param msgBody
     */
    private void saveAttachment(MsgBody msgBody, String messageId) {
        List<MsgAttachment> msgAttachmentList = msgBody.getAttachment();
        if (msgAttachmentList == null || msgAttachmentList.size() == 0) {
            return;
        }

        List<SiteMessageAttachment> messageAttachmentList = new ArrayList<>(msgAttachmentList.size());

        SiteMessageAttachment attachment = new SiteMessageAttachment();
        attachment.setId(StringUtil.getUUID());
        attachment.setCreateUser(msgBody.getSendUserId());
        attachment.setCreateTime(DateUtil.getNowDate());
        attachment.setDelFlag(0);
        attachment.setRefMessageId(messageId);

        for (MsgAttachment msgAttachment : msgAttachmentList) {
            attachment.setFileId(msgAttachment.getFileId());
            attachment.setFileName(msgAttachment.getFileName());
            messageAttachmentList.add(attachment);
        }

        siteMessageAttachmentDao.insert(messageAttachmentList);
    }

    /**
     * 获取附件列表
     *
     * @param messageId 消息id
     * @return
     */
    private List<Map> getAttachmentList(String messageId) {
        List<SiteMessageAttachment> attachmentList = siteMessageAttachmentDao.selectAllByMessageId(messageId);
        List<Map> mapList = new ArrayList<>(attachmentList.size());
        Map<String, String> map;
        for (SiteMessageAttachment attachment : attachmentList) {
            map = new HashMap<>(2);
            map.put("id", attachment.getFileId());
            map.put("name", attachment.getFileName());
            mapList.add(map);
        }
        return mapList;
    }
}
