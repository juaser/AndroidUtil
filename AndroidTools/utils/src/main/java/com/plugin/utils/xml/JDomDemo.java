package com.plugin.utils.xml;

import com.plugin.utils.log.LogUtils;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Description: JDOM 解析XML文档
 * @Author: zxl
 * @Date: 30/8/16.
 */
public class JDomDemo{
    private Document document = null;
    private SAXBuilder builder = new SAXBuilder();

    public void parserXml(String fileName) throws JDOMException, IOException {
        document = builder.build(fileName);
        operateDocument(document);
    }

    public void parserXml(InputStream is) throws JDOMException, IOException {
        document = builder.build(is);
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
        List userList = users.getChildren("user");
        for (int i = 0; i < userList.size(); i++) {
            Element user = (Element) userList.get(i);
            List userInfo = user.getChildren();
            for (int j = 0; j < userInfo.size(); j++) {
                LogUtils.e(((Element) userInfo.get(j)).getName()
                        + ":" + ((Element) userInfo.get(j)).getValue());

            }
        }
    }
}
