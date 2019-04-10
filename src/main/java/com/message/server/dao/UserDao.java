package com.message.server.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户Dao
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/8/6 11:18
 */
public interface UserDao {
    /**
     * 根据用户组code获取用户id列表
     *
     * @param groupCode 用户组code
     * @return 用户id列表
     */
    List<Map<String, String>> selectByGroupCode(@Param("groupCode") String groupCode);

    /**
     * 根据角色code获取用户id列表
     *
     * @param roleCode 角色code
     * @return 用户id列表
     */
    List<Map<String, String>> selectByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 获取用户名
     *
     * @param id 用户id
     * @return 用户名
     */
    String selectUserNameById(String id);
}
