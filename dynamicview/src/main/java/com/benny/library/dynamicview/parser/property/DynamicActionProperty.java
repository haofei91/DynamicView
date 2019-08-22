package com.benny.library.dynamicview.parser.property;

import com.benny.library.dynamicview.action.ActionProcessor;
import com.benny.library.dynamicview.view.DynamicViewBuilder;

import org.json.JSONObject;

import java.util.Map;

/**
 * 基于动态值的点击事件
 */
public class DynamicActionProperty {
    private String key;//onClick  事件名
    private String value;//serverdata  服务端数据中对应事件标示的key，会被传入ActionProcessor


    public DynamicActionProperty(String key, String value) {
        this.key = key;
        this.value = value.substring(2, value.length() - 2);
    }

    public static boolean canHandle(String key, String value) {
        return key.startsWith("on") && value.length() > 4 && value.startsWith("({") && value.endsWith("})");
    }


    /**
     * 为控件属性赋值，需要映射动态值
     *
     * 如果 服务端的数据 中 包含当前属性的valueKey， 则将服务端数据中valueKey对应的具体数值，复制给构造器的key属性
     * @param builder
     * @param data
     */
    public void set(DynamicViewBuilder builder, ActionProcessor processor, JSONObject data) {
        builder.setAction(key, data.optString(value), processor);
    }

    public void set(DynamicViewBuilder builder, ActionProcessor processor, Map<String, String> data) {
        builder.setAction(key, data.get(value), processor);
    }
}
