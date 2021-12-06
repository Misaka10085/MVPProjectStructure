package com.misakanetwork.lib_common.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created By：Misaka10085
 * on：2021/4/13
 * package：com.misakanetwork.lib_common.entity
 * class name：MessageEvent
 * desc：RxBus MessageEvent
 */
public class MessageEvent {
    // Test
    public static final String EVENT_TEST_HINT = "EVENT_TEST_HINT";
    // End
    // 富文本图加载完成HtmlFromUtils.java
    public static final String EVENT_HTML_FROM_FINISHED = "EVENT_HTML_FROM_FINISHED";
    // 微信支付成功
    public static final String EVENT_WX_PAYMENT_SUCCESS = "EVENT_WX_PAYMENT_SUCCESS";
    // 微信支付失败
    public static final String EVENT_WX_PAYMENT_FAILED = "EVENT_WX_PAYMENT_FAILED";
    // 微信成功获取到code
    public static final String EVENT_GET_WX_CODE = "EVENT_GET_WX_CODE";
    // 微信接口返回信息
    public static final String EVENT_WX_INFO = "EVENT_WX_INFO";

    private String event;
    private Map<String, Object> map = new HashMap<>();

    public MessageEvent() {
    }

    public MessageEvent(String event) {
        this.event = event;
    }

    public MessageEvent put(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public Object get(String key) {
        return map.get(key);
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
