package com.wl.wlp2ploansystem.infrastructures.common.notification;

//https://www.cnblogs.com/hhhshct/p/8849449.html
//https://blog.csdn.net/elonpage/article/details/78446695

/**
 * 消息通知服务接口
 */
public interface NotificationSevice {
    /**
     * 通知特定人
     * @param user 特定人
     * @param payload 参数
     */
    public void notify(String user,Object payload);

    /**
     * 全员通知
     * @param payload 参数
     */
    public void nofityAll(Object payload);
}