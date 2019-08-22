package com.benny.library.dynamicview.parser.property;

import com.benny.library.dynamicview.view.DynamicViewBuilder;

import org.json.JSONObject;

import java.util.Map;

/**
 * 动态值属性
 */
public class DynamicProperty {
    private String key;
    private String valueKey;


    public DynamicProperty(String key, String valueKey) {
        this.key = key;
        this.valueKey = valueKey.substring(1, valueKey.length() - 1);
    }

    /**
     * 判断是否是当前类型
     * @param key
     * @param value
     * @return
     */
    public static boolean canHandle(String key, String value) {
        return value.length() > 2 && value.startsWith("{") && value.endsWith("}");
    }


    /**
     * 为控件属性赋值，需要映射动态值
     *
     * 如果 服务端的数据 中 包含当前属性的valueKey， 则将服务端数据中valueKey对应的具体数值，复制给构造器的key属性
     * @param builder
     * @param data
     */
    public void set(DynamicViewBuilder builder, Map<String, String> data) {
        try {
            if (data.containsKey(valueKey)) {
                builder.setProperty(key, data.get(valueKey));
            }
        }
        catch (Exception ignored) {
        }
    }

    public void set(DynamicViewBuilder builder, JSONObject data) {
        try {
            if (data.has(valueKey)) {
                builder.setProperty(key, data.optString(valueKey));
            }
        }
        catch (Exception ignored) {
        }
    }
}
