package com.benny.library.dynamicview.parser.property;

import com.benny.library.dynamicview.view.DynamicViewBuilder;

/**
 * 静态属性
 */
public class StaticProperty {
    private String key;
    private String value;

    public StaticProperty(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 为控件属性赋值
     */
    public void set(DynamicViewBuilder builder) {
        try {
            builder.setProperty(key, value);
        }
        catch (Exception ignored) {
        }
    }

    public String getValue() {
        return value;
    }
}
