package com.benny.library.dynamicview.view;

public class ViewType {

    /**
     * 表示此节点为View，不能包含子节点
     */
    public interface View {
    }


    /**
     * 表示此节点为容器，可包含子节点，参照LinearLayout、RelativeLayout等
     */
    public interface GroupView {
    }

    /**
     * 此节点只能有一个子节点，根据数据来决定生成多少子view，参照ListView、RecyclerView等
     */
    public interface AdapterView {

        /**
         * 数据，，， 获取view上的tag---得到ViewBinder--- 再次触发绑定数据
         * @param source
         */
        void setDataSource(String source);

        /**
         * 会被作为inflate方法，传递到复用型view的onCreateViewHolder方法中
         * @param inflater
         */
        void setInflater(ViewInflater inflater);
    }
}
