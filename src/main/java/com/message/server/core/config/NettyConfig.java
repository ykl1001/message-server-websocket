package com.message.server.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取yml配置文件中的信息
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/5/28 13:39
 */
@Component
@ConfigurationProperties(prefix = "netty")
public class NettyConfig {
    /**
     * Netty 运行端口
     */
    private int port;

    /**
     * 最大线程量
     */
    private int maxQueueSize;

    /**
     * 数据包最大长度
     */
    private int maxFrameLength;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxQueueSize() {
        return maxQueueSize;
    }

    public void setMaxQueueSize(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
    }

    public int getMaxFrameLength() {
        return maxFrameLength;
    }

    public void setMaxFrameLength(int maxFrameLength) {
        this.maxFrameLength = maxFrameLength;
    }
}
