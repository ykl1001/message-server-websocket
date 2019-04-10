package com.message.server.core.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程工具
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/8/6 17:45
 */
public final class ThreadUtil {
    /**
     * 核心线程池大小
     */
    private static final int CORE_POOL_SIZE = 10;

    /**
     * 最大线程池大小
     */
    private static final int MAX_POOL_SIZE = 200;

    /**
     * 线程池中超过corePoolSize数目的空闲线程最大存活时间
     */
    private static final Long KEEP_ALIVE_TIME = 30L;

    private static final int LINKED_BLOCKING_QUEUE_SIZE = 1024;

    private ThreadUtil() {
        // 有意留空，不做任何处理
    }

    /**
     * 获取线程池
     *
     * @return
     */
    public static ExecutorService getExecutorService() {
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(LINKED_BLOCKING_QUEUE_SIZE), new ThreadPoolExecutor.AbortPolicy());
    }
}
