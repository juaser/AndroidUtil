package com.plugin.utils.xml;

import com.plugin.utils.log.LogUtils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;

/**
 * @Description: Dom4j 解析XML文档
 * @Author: zxl
 * @Date: 30/8/16.
 */
public class Dom4jDemo {
    private SAXReader saxReader = new SAXReader();
    private Document document;
    private static volatile Dom4jDemo mInstance = null;

    private Dom4jDemo() {
    }

    public static Dom4jDemo getInstance() {
        Dom4jDemo instance = mInstance;
        if (instance == null) {
            synchronized (Dom4jDemo.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new Dom4jDemo();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    public void parserXml(String fileName) throws DocumentException {
        document = saxReader.read(new File(fileName));
    }

    public void parserXml(InputStream is) throws DocumentException {
        document = saxReader.read(is);
        operateDocument(document);
    }

    /**
     * 解析获取的Document
     *
     * @param document
     */
    public void operateDocument(Document document) {
        if (document == null) {
            return;
        }
        Element users = document.getRootElement();
        for (Iterator i = users.elementIterator(); i.hasNext(); ) {
            Element user = (Element) i.next();
            for (Iterator j = user.elementIterator(); j.hasNext(); ) {
                Element node = (Element) j.next();
                LogUtils.e(node.getName() + ":" + node.getText());
            }
        }
    }
}
