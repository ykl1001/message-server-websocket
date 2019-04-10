package com.message.server.core.util;

/**
 * 自定义线程执行
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/8/6 17:22
 */
public abstract class BaseDynamicRunnable implements Runnable {
    @Override
    public void run() {
        doSth();
    }

    /**
     * 自定义线程执行函数
     */
    public abstract void doSth();
}
