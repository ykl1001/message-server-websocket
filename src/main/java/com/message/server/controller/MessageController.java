package com.message.server.controller;

import com.message.server.dao.SiteMessageDao;
import com.message.server.model.*;
import com.message.server.service.SiteMessageService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 站内消息管理
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/6/4 14:01
 */
@RestController
@RequestMapping(value = "/message", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(value = "/message", tags = "站内消息", description = "站内消息相关接口")
@ApiResponses({
        @ApiResponse(code = 200, message = "成功"),
        @ApiResponse(code = 401, message = "token未授权"),
        @ApiResponse(code = 403, message = "禁止访问"),
        @ApiResponse(code = 404, message = "请求路径不存在"),
        @ApiResponse(code = 406, message = "数据格式不正确"),
        @ApiResponse(code = 410, message = "数据不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
})
public class MessageController {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    private static final String TOKEN = "Authorization";

    @Autowired
    private SiteMessageService siteMessageService;

    @Resource
    private SiteMessageDao siteMessageDao;

    @GetMapping(value = "/list")
    @ApiOperation(value = "获取消息表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(paramType = "query", name = "status", value = "阅读状态，1-已阅读，0-未阅读，-1=全部消息", required = true),
            @ApiImplicitParam(paramType = "query", name = "categoryId", value = "消息分类id，0=全部消息，1-流程消息，2-通知公告，100-系统消息", required = true),
            @ApiImplicitParam(paramType = "query", name = "pageNum", value = "当前页码", required = true, defaultValue = "1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示数据条数", required = true, defaultValue = "10", dataType = "int")
    })
    public ReturnT<Map> getMessageList(HttpServletRequest request, String userId, int status, int categoryId, int pageNum, int pageSize) {
        String token = request.getHeader(TOKEN);
        if (userId == null) {
            return new ReturnT<>(ReturnCode.UNAUTHORIZED);
        }
        Map<String, Object> map = siteMessageService.selectPage(userId, status, categoryId, pageNum, pageSize);
        return new ReturnT<>(ReturnCode.SUCCESS, "成功", map);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "获取消息详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true)
    })
    @ApiImplicitParam(paramType = "path", name = "id", value = "消息id", required = true)
    public ReturnT<Map> getMessageDetails(HttpServletRequest request, @PathVariable("id") String id, String userId) {
        String token = request.getHeader(TOKEN);
        if (userId == null) {
            return new ReturnT<>(ReturnCode.UNAUTHORIZED);
        }
        Map<String, Object> message = siteMessageService.selectOneByUserId(id, userId);
        if (message == null) {
            return new ReturnT<>(ReturnCode.DATA_NOT_EXIST);
        }
        return new ReturnT<>(ReturnCode.SUCCESS, "成功", message);
    }

    @GetMapping(value = "/count/total")
    @ApiOperation(value = "获取全部消息数量")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true)
    })
    public ReturnT<List> getTotalCount(HttpServletRequest request, String userId) {
        if (userId == null) {
            return new ReturnT<>(ReturnCode.UNAUTHORIZED);
        }
        List<Map> list = siteMessageService.selectTotalCount(userId);
        return new ReturnT<>(ReturnCode.SUCCESS, "成功", list);
    }

    @GetMapping(value = "/count/new")
    @ApiOperation(value = "获取新消息总数量")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true)
    })
    public ReturnT<Integer> getNewCount(HttpServletRequest request, String userId) {
        if (userId == null) {
            return new ReturnT<>(ReturnCode.UNAUTHORIZED);
        }
        int count = siteMessageService.selectNewCount(userId);
        return new ReturnT<>(ReturnCode.SUCCESS, "成功", count);
    }

    @DeleteMapping
    @ApiOperation(value = "删除消息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "消息id，多个id以逗号隔开", required = true),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true)
    })
    public ReturnT<String> deleteMessage(HttpServletRequest request, String userId, String... ids) {
        if (userId == null) {
            return new ReturnT<>(ReturnCode.UNAUTHORIZED);
        }

        int count = siteMessageService.deleteMessage(userId, ids);
        if (count > 0) {
            return ReturnT.SUCCESS;
        }

        return ReturnT.FAIL;
    }

    @PostMapping(value = "/read/sign")
    @ApiOperation(value = "标记已读")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "消息id，多个id以逗号隔开", required = true),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true)
    })
    public ReturnT<String> signMessageRead(HttpServletRequest request, String userId, String... ids) {
        if (userId == null) {
            return new ReturnT<>(ReturnCode.UNAUTHORIZED);
        }

        List<String> idList = new ArrayList<>();
        if (ids != null && ids.length > 0) {
            idList = Arrays.asList(ids);
        }
        siteMessageService.signRead(userId, idList);
        return ReturnT.SUCCESS;
    }

    @PostMapping(value = "/read/all")
    @ApiOperation(value = "全部已读")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "categoryId", value = "消息分类id，0=全部消息，1-流程消息", required = true),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true)
    })
    public ReturnT<String> allMessageRead(HttpServletRequest request, int categoryId, String userId) {
        if (userId == null) {
            return new ReturnT<>(ReturnCode.UNAUTHORIZED);
        }

        List<SiteMessage> messageList = siteMessageDao.selectAllByUserId(userId, 0, categoryId);
        if (messageList.isEmpty()) {
            return ReturnT.FAIL;
        }

        List<String> idList = new ArrayList<>(messageList.size());
        for (int i = 0; i < messageList.size(); i++) {
            idList.add(messageList.get(i).getId());
        }

        int count = siteMessageService.signRead(userId, idList);
        if (count > 0) {
            return ReturnT.SUCCESS;
        }

        return ReturnT.FAIL;
    }

    @PostMapping(value = "/push")
    @ApiOperation(value = "消息推送")
    public ReturnT<String> messagePush(@RequestBody MsgBody msgBody) {
        siteMessageService.insert(msgBody);
        return ReturnT.SUCCESS;
    }
}
