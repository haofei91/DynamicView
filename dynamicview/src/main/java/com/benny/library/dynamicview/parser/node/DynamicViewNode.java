package com.benny.library.dynamicview.parser.node;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.benny.library.dynamicview.parser.property.NodeProperties;
import com.benny.library.dynamicview.view.DynamicViewBuilder;
import com.benny.library.dynamicview.view.DynamicViewBuilderFactory;
import com.benny.library.dynamicview.view.ViewBinder;

/**
 * 节点
 * 表示此节点为View，不能包含子节点
 */
public class DynamicViewNode {
    private DynamicViewNode parent;

    protected String name;
    protected NodeProperties properties;


    /********** 构造函数 *******/

    public DynamicViewNode(String className, NodeProperties properties) {
        this.name = className;
        this.properties = properties;
    }


    /********** 容器类节点需要实现 *******/

    public void addChild(DynamicViewNode child) {
        throw new RuntimeException("View node " + name + " dose not allow add child");
    }

    /********** 生成ui视图 + 绑定属性 + 绑定事件 *******/

    public View createView(Context context, ViewGroup parent, ViewBinder viewBinder) throws Exception {

        //1. 实例化构造器，并触发createView(Context context) 可以 构建对应的 真实UI布局
        //2. ui布局存储在 实例中
        DynamicViewBuilder builder = DynamicViewBuilderFactory.create(context, name);

        // first add to parent, then set static property, for create correct LayoutParameter
        if (parent != null) {
            parent.addView(builder.getView());
        }

        //3. 注册  视图构造器 和 属性的关联关系
        viewBinder.add(builder, properties);

        //4. 处理静态属性值
        //5. 处理点击事件
        //ps: 动态相关的，需要在数据绑定时，自行触发
        properties.set(builder);
        properties.setAction(builder, viewBinder.getActionProcessor());


        return builder.getView();
    }



    /********** get |set *******/


    protected void setParent(DynamicViewNode parent) {
        this.parent = parent;
    }
    public DynamicViewNode getParent() {
        return parent;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public String getProperty(String key) {
        return properties.get(key);
    }


}
