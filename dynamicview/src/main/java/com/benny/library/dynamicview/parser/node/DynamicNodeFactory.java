package com.benny.library.dynamicview.parser.node;

import com.benny.library.dynamicview.view.ViewType;
import com.benny.library.dynamicview.parser.property.NodeProperties;
import com.benny.library.dynamicview.view.DynamicViewBuilderFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 节点工厂
 */
public class DynamicNodeFactory {
    private static Map<Class, Class> viewTypeMap = new HashMap<>();

    /**
     * 1. 获取 对应节点的DynamicViewBuilder Class
     * 2. 根据具体的节点名称 Hbox，生成节点，复制属性
     * @param name  HBox
     * @param properties
     * @return
     * @throws Exception
     */
    public static DynamicViewNode create(String name, NodeProperties properties) throws Exception {

            //1. 获取 对应节点的DynamicViewBuilder Class
            Class<?> clazz = DynamicViewBuilderFactory.register(name);

            //2. 根据具体的节点名称构建指定节点
            Class<?> viewType = getViewType(clazz);
            if (viewType == ViewType.View.class) {
                return new DynamicViewNode(name, properties);
            }
            else if (viewType == ViewType.GroupView.class) {
                return new DynamicGroupViewNode(name, properties);
            }
            else if (viewType == ViewType.AdapterView.class) {
                return new DynamicAdapterViewNode(name, properties);
            }
            throw new Exception("Unknown node " + name);
    }


    /**
     * 根据具体的节点名称 Hbox，转换为对应节点类型 GroupView
     * @param clazz
     * @return
     */
    private static Class<?> getViewType(Class<?> clazz) {
        Class<?> viewType = viewTypeMap.get(clazz);
        if (viewType != null) {
            return viewType;
        }

        if (ViewType.View.class.isAssignableFrom(clazz)) {
            viewTypeMap.put(clazz, ViewType.View.class);
        }
        else if (ViewType.GroupView.class.isAssignableFrom(clazz)) {
            viewTypeMap.put(clazz, ViewType.GroupView.class);
        }
        else if (ViewType.AdapterView.class.isAssignableFrom(clazz)) {
            viewTypeMap.put(clazz, ViewType.AdapterView.class);
        }
        return viewTypeMap.get(clazz);
    }
}
