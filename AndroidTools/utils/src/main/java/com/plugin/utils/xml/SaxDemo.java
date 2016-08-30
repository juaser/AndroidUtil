package com.plugin.utils.xml;

import com.plugin.utils.log.LogUtils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @Description: SAX 解析XML文档
 * @Author: zxl
 * @Date: 30/8/16.
 */
public class SaxDemo {
    private SAXParserFactory saxfac = SAXParserFactory.newInstance();
    private SAXParser saxparser = null;

    public void parserXml(String fileName) throws ParserConfigurationException, SAXException, IOException {
        saxparser = saxfac.newSAXParser();
        saxparser.parse(new FileInputStream(fileName), new MySAXHandler());
    }

    public void parserXml(InputStream is) throws ParserConfigurationException, SAXException, IOException {
        saxparser = saxfac.newSAXParser();
        saxparser.parse(is, new MySAXHandler());
    }

    private class MySAXHandler extends DefaultHandler {
        boolean hasAttribute = false;
        Attributes attributes = null;

        public void startDocument() throws SAXException {
            LogUtils.e("文档开始打印了");
        }

        public void endDocument() throws SAXException {
            LogUtils.e("文档打印结束了");
        }

        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            if (qName.equals("users")) {
                return;
            }
            if (qName.equals("user")) {
                return;
            }
            if (attributes.getLength() > 0) {
                this.attributes = attributes;
                this.hasAttribute = true;
            }
        }

        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            if (hasAttribute && (attributes != null)) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    LogUtils.e(attributes.getQName(0) + ":"
                            + attributes.getValue(0));
                }
            }
        }

        public void characters(char[] ch, int start, int length)
                throws SAXException {
            LogUtils.e(new String(ch, start, length));
        }
    }

}
