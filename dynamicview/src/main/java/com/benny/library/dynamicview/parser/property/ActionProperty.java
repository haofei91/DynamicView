package com.benny.library.dynamicview.parser.property;

import com.benny.library.dynamicview.action.ActionProcessor;
import com.benny.library.dynamicview.view.DynamicViewBuilder;

/**
 * 基于静态值的点击事件
 */
public class ActionProperty {
    private String key;//onClick  事件名
    private String value;//VALUE_CLICK  事件标示，会被传入ActionProcessor


    public ActionProperty(String key, String value) {
        this.key = key;
        this.value = value.substring(1, value.length() - 1);
    }


    public static boolean canHandle(String key, String value) {
        return key.startsWith("on") && value.length() > 2 && value.startsWith("(") && value.endsWith(")");
    }



    public void set(DynamicViewBuilder builder, ActionProcessor processor) {
        builder.setAction(key, value, processor);
    }
}
