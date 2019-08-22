package com.benny.library.dynamicview.parser.property;

import android.text.TextUtils;

import com.benny.library.dynamicview.action.ActionProcessor;
import com.benny.library.dynamicview.util.ViewIdGenerator;
import com.benny.library.dynamicview.view.DynamicViewBuilder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 节点的属性
 */
public class NodeProperties {
    private ViewIdGenerator idGenerator;
    private Map<String, StaticProperty> staticProperties = new HashMap<>();//字面值静态属性
    private Map<String, DynamicProperty> dynamicProperties = new HashMap<>();//动态值属性
    private Map<String, ActionProperty> actions = new HashMap<>();//基于静态值的点击事件
    private Map<String, DynamicActionProperty> dynamicActions = new HashMap<>();//基于动态值的点击事件

    public NodeProperties(ViewIdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public void add(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            if (DynamicActionProperty.canHandle(key, value)) {
                dynamicActions.put(key, new DynamicActionProperty(key, value));
            }
            else if (ActionProperty.canHandle(key, value)) {
                actions.put(key, new ActionProperty(key, value));
            }
            else if (DynamicProperty.canHandle(key, value)) {
                dynamicProperties.put(key, new DynamicProperty(key, value));
            } else {
                processStaticProperty(key, value);
            }
        }
    }

    private void processStaticProperty(String key, String value) {
        if (key.equals("name")) {
            staticProperties.put("id", new StaticProperty("id", idGenerator.getId(value)));
        }
        else if (value.startsWith("@")) {
            String relatedName = value.substring(1);
            if (idGenerator.contains(relatedName)) {
                staticProperties.put(key, new StaticProperty(key, idGenerator.getId(relatedName)));
            }
        }
        else {
            staticProperties.put(key, new StaticProperty(key, value));
        }
    }

    /**
     * 获取指定key对应的值
     * @param key
     * @return
     */
    public String get(String key) {
        return staticProperties.containsKey(key) ? staticProperties.get(key).getValue() : null;
    }


    /**
     * 为控件属性赋值
     */
    public void set(DynamicViewBuilder builder) {
        for (Map.Entry<String, StaticProperty> entry : staticProperties.entrySet()) {
            entry.getValue().set(builder);
        }
    }

    /**
     * 为控件属性赋事件处理
     */
    public void setAction(DynamicViewBuilder builder, ActionProcessor processor) {
        if (!actions.isEmpty()) {
            for (Map.Entry<String, ActionProperty> entry : actions.entrySet()) {
                entry.getValue().set(builder, processor);
            }
        }
    }

    /**
     * 为控件属性赋值，需要映射动态值
     *
     * 如果 服务端的数据 中 包含当前属性的valueKey， 则将服务端数据中valueKey对应的具体数值，复制给构造器的key属性
     * @param builder
     */
    public void set(DynamicViewBuilder builder, ActionProcessor processor, Map<String, String> data) {
        for (Map.Entry<String, DynamicProperty> entry : dynamicProperties.entrySet()) {
            entry.getValue().set(builder, data);
        }

        for (Map.Entry<String, DynamicActionProperty> entry : dynamicActions.entrySet()) {
            entry.getValue().set(builder, processor, data);
        }
    }

    public void set(DynamicViewBuilder builder, ActionProcessor processor, JSONObject data) {
        for (Map.Entry<String, DynamicProperty> entry : dynamicProperties.entrySet()) {
            entry.getValue().set(builder, data);
        }

        for (Map.Entry<String, DynamicActionProperty> entry : dynamicActions.entrySet()) {
            entry.getValue().set(builder, processor, data);
        }
    }


}
