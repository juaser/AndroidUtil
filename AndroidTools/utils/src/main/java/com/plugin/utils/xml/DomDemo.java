package com.plugin.utils.xml;

import com.plugin.utils.log.LogUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * @Description: DOM 解析XML文档
 * @Author: zxl
 * @Date: 30/8/16.
 */
public class DomDemo {

    private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    private DocumentBuilder db = null;
    private Document document = null;
    private static volatile DomDemo mInstance = null;

    private DomDemo() {
    }

    public static DomDemo getInstance() {
        DomDemo instance = mInstance;
        if (instance == null) {
            synchronized (DomDemo.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new DomDemo();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    public void parserXml(String fileName) throws ParserConfigurationException, IOException, SAXException {
        db = dbf.newDocumentBuilder();
        document = db.parse(fileName);
        operateDocument(document);
    }

    public void parserXml(InputStream is) throws ParserConfigurationException, IOException, SAXException {
        db = dbf.newDocumentBuilder();
        document = db.parse(is);
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
        NodeList users = document.getChildNodes();
        for (int i = 0; i < users.getLength(); i++) {
            Node user = users.item(i);
            NodeList userInfo = user.getChildNodes();
            for (int j = 0; j < userInfo.getLength(); j++) {
                Node node = userInfo.item(j);
                NodeList userMeta = node.getChildNodes();

                for (int k = 0; k < userMeta.getLength(); k++) {
                    if (userMeta.item(k).getNodeName() != "#text")
                        LogUtils.e(userMeta.item(k).getNodeName()
                                + ":" + userMeta.item(k).getTextContent());
                }
            }
        }
    }
}
