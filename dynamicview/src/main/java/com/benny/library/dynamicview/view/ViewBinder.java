package com.benny.library.dynamicview.view;

import android.util.Pair;
import android.view.View;

import com.benny.library.dynamicview.action.ActionProcessor;
import com.benny.library.dynamicview.parser.property.NodeProperties;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewBinder {

    /**
     * 存储 视图构造器 和 属性 的关联关系。。。。需要在绑定数据时使用
     * @param builder
     * @param properties
     */

    private List<Pair<DynamicViewBuilder, NodeProperties>> pairs = new ArrayList<>();

    public void add(DynamicViewBuilder builder, NodeProperties properties) {
        pairs.add(Pair.create(builder, properties));
    }

    /**
     * 事件处理
     */

    private ActionProcessorWrapper actionProcessorWrapper = new ActionProcessorWrapper();

    public void setActionProcessor(ActionProcessor processor) {
        actionProcessorWrapper.setProcessor(processor);
    }

    public ActionProcessor getActionProcessor() {
        return actionProcessorWrapper;
    }

    private static class ActionProcessorWrapper implements ActionProcessor {
        private ActionProcessor processor;

        public void setProcessor(ActionProcessor processor) {
            this.processor = processor;
        }

        @Override
        public void processAction(View view, String tag, Object... data) {
            if (processor != null) {
                processor.processAction(view, tag, data);
            }
        }
    }



    /**
     * 绑定动态数据，为控件属性赋值
     * @param data
     */
    public void bind(JSONObject data) {
        for (Pair<DynamicViewBuilder, NodeProperties> pair : pairs) {
            pair.second.set(pair.first, actionProcessorWrapper, data);
        }
    }

    public void bind(Map<String, String> data) {
        for (Pair<DynamicViewBuilder, NodeProperties> pair : pairs) {
            pair.second.set(pair.first, actionProcessorWrapper, data);
        }
    }




}
