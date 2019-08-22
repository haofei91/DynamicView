package com.benny.library.dynamicview.parser;

import android.text.TextUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.benny.library.dynamicview.parser.node.DynamicNodeFactory;
import com.benny.library.dynamicview.parser.node.DynamicViewNode;
import com.benny.library.dynamicview.parser.property.NodeProperties;
import com.benny.library.dynamicview.util.ViewIdGenerator;

import java.io.StringReader;

/**
 * XML的解析器，转化为DynamicViewNode 节点树
 */
public class XMLLayoutParser {
    private XmlPullParser parser;
    private SerialNumberHandler serialNumberHandler;

    public XMLLayoutParser(SerialNumberHandler serialNumberHandler) {
        try {
            this.serialNumberHandler = serialNumberHandler;
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            parser = factory.newPullParser();
        }
        catch (Exception ignored) {
        }
    }

    /**
     * 解析文档
     */
    public synchronized DynamicViewTree parseDocument(String xml) throws Exception {
        ViewIdGenerator viewIdGenerator = new ViewIdGenerator();

        parser.setInput(new StringReader(xml));
        DynamicViewNode currentNode = null;
        for (int event; (event = parser.getEventType()) != XmlPullParser.END_DOCUMENT; ) {
            if (event == XmlPullParser.START_TAG) {
                DynamicViewNode viewNode = parseNode(parser, viewIdGenerator);
                if (currentNode != null) {
                    currentNode.addChild(viewNode);
                }
                currentNode = viewNode;

                //判断缓存
                if (viewNode.isRoot()) {
                    String serialNumber = viewNode.getProperty("sn");
                    if (TextUtils.isEmpty(serialNumber)) {
                        throw new XmlPullParserException("sn does not defined in root node");
                    }
                    DynamicViewTree viewTree = serialNumberHandler.onReceive(serialNumber);
                    if (viewTree != null) {
                        return viewTree;
                    }
                }
            }
            else if (event == XmlPullParser.END_TAG) {
                if (currentNode != null && !currentNode.isRoot()) {
                    currentNode = currentNode.getParent();
                }
            }
            parser.next();
        }
        return new DynamicViewTree(currentNode);
    }

    //构建节点
    private DynamicViewNode parseNode(XmlPullParser parser, ViewIdGenerator viewIdGenerator) throws Exception {
        String className = parser.getName();
        NodeProperties properties = parseAttributes(parser, viewIdGenerator);

        //1. 获取 对应节点的DynamicViewBuilder Class
        //2.  根据具体的节点名称 Hbox，生成节点DynamicViewNode，赋值属性
        return DynamicNodeFactory.create(className, properties);
    }

    //解析节点的属性
    private NodeProperties parseAttributes(XmlPullParser parser, ViewIdGenerator viewIdGenerator) {
        NodeProperties properties = new NodeProperties(viewIdGenerator);
        int attrCount = parser.getAttributeCount();
        for (int i = 0; i < attrCount; ++i) {
            properties.add(parser.getAttributeName(i), parser.getAttributeValue(i));
        }
        return properties;
    }


    /**
     * 缓存获取手柄
     */
    public interface SerialNumberHandler {
        DynamicViewTree onReceive(String serialNumber);
    }
}
