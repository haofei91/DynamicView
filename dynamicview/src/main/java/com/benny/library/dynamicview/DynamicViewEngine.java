package com.benny.library.dynamicview;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.benny.library.dynamicview.action.ActionProcessor;
import com.benny.library.dynamicview.parser.XMLLayoutParser;
import com.benny.library.dynamicview.parser.DynamicViewTree;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 对外接口
 * 1. 构建DynamicViewTree节点树，这个是核心
 * 2. 使用节点树生成view
 * 3. 使用节点树绑定数据
 * 4. 使用节点树绑定事件
 */
public class DynamicViewEngine implements XMLLayoutParser.SerialNumberHandler {

    private XMLLayoutParser parser = new XMLLayoutParser(this);

    /*************** 单利模式  ***********************/
    public static DynamicViewEngine getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final DynamicViewEngine INSTANCE = new DynamicViewEngine();
    }

    /*************** 生成：构建 ui视图  ***********************/

    public View inflate(Context context, ViewGroup parent, String xml) {
        long tick = System.currentTimeMillis();
        try {
            DynamicViewTree viewTree = compile(xml);
            return viewTree.inflate(context, parent);
        }
        catch (Exception e) {
            Log.e("DynamicViewEngine", "inflate Exception: " + e);
            return null;
        }
        finally {
            Log.i("DynamicViewEngine", "inflate cost " + (System.currentTimeMillis() - tick));
        }
    }


    public DynamicViewTree compile(String xml) throws Exception {
        DynamicViewTree viewTree = parser.parseDocument(xml);
        String serialNumber = viewTree.getRoot().getProperty("sn");//唯一标示
        if (!viewTreeMap.containsKey(serialNumber)) {
            viewTreeMap.put(serialNumber, viewTree);
        }
        return viewTree;
    }

    /*************** 绑定数据  ***********************/

    public static void bindView(View view, Map<String, String> data) {
        DynamicViewTree.bindView(view, data);
    }

    public static void bindView(View view, JSONObject data) {
        DynamicViewTree.bindView(view, data);
    }

    /*************** 绑定事件  ***********************/

    public static void setActionProcessor(View view, ActionProcessor processor) {
        DynamicViewTree.setActionProcessor(view, processor);
    }




    /*************** 缓存  ***********************/

    private Map<String, DynamicViewTree> viewTreeMap = new HashMap<>();

    /**
     * 解析过程中调用，如果已解析，则直接返回
     * @param serialNumber 唯一标示
     * @return
     */
    @Override
    public DynamicViewTree onReceive(String serialNumber) {
        return viewTreeMap.get(serialNumber);
    }


}
