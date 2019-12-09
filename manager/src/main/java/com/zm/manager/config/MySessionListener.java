package com.zm.manager.config;

import org.apache.log4j.Logger;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 监听访问
 */
public class MySessionListener implements SessionListener {
    private static Logger log = Logger.getLogger(MySessionListener.class);
    private final AtomicInteger sessionCount = new AtomicInteger(0);

    @Override
    public void onStart(Session session) {
        sessionCount.incrementAndGet();
        log.info("登录====="+sessionCount.get());
    }

    @Override
    public void onStop(Session session) {
        sessionCount.decrementAndGet();
        log.info("登录退出====="+sessionCount.get());
    }

    @Override
    public void onExpiration(Session session) {
        sessionCount.decrementAndGet();
        log.info("登录过期===="+sessionCount.get());
    }

    public int getSessionCount() {
        return sessionCount.get();
    }
}
