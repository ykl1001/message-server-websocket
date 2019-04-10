package com.message.server.core.config;

import com.message.server.websocket.WebSocketHandler;
import com.message.server.websocket.WebSocketServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * websocket配置
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/6/22 09:04
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public WebSocketHandler getWebSocketHandler() {
        return new WebSocketHandler();
    }

    @Bean(initMethod = "init")
    public WebSocketServer initWebSocketServer() {
        return new WebSocketServer();
    }
}
