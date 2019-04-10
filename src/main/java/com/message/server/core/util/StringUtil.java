package com.message.server.core.util;

import java.util.UUID;

/**
 * 字符串帮助类
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/6/22 08:36
 */
public final class StringUtil {
    private StringUtil() {
        // 有意留空，不做任何处理
    }

    /**
     * 获取uuid
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 进化Token
     *
     * @param token 客户端输入Token
     * @return 返回进化后的Token
     */
    public static String evolutionToken(String token) {
        if (token == null || token.isEmpty()) {
            return "";
        }
        return token.replaceFirst("bearer", "").trim();
    }
}
