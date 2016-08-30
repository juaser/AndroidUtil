package com.zxl.androidtools.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.plugin.utils.base.BaseActivity;
import com.plugin.utils.log.LogUtils;
import com.plugin.utils.xml.Dom4jDemo;
import com.plugin.utils.xml.DomDemo;
import com.plugin.utils.xml.JDomDemo;
import com.plugin.utils.xml.SaxDemo;

import org.dom4j.DocumentException;
import org.jdom2.JDOMException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

/**
 * @Description:
 * @Author: zxl
 * @Date: 30/8/16.
 */
public class TestXmlActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        domXml();
        jdomXml();
        dom4jXml();
        saxXml();
    }

    public void domXml() {
        DomDemo domdemo = new DomDemo();
        try {
            InputStream open = getAssets().open("userdemo.xml");
            domdemo.parserXml(open);
        } catch (IOException e) {
            LogUtils.e("IOException");
        } catch (ParserConfigurationException e) {
            LogUtils.e("ParserConfigurationException");
        } catch (SAXException e) {
            LogUtils.e("SAXException");
        }
    }

    public void jdomXml() {
        JDomDemo jdomdemo = new JDomDemo();
        try {
            InputStream open = getAssets().open("userdemo.xml");
            jdomdemo.parserXml(open);
        } catch (IOException e) {
            LogUtils.e("IOException");
        } catch (JDOMException e) {
            LogUtils.e("JDOMException");
        }
    }

    public void dom4jXml() {
        Dom4jDemo dom4jdemo = new Dom4jDemo();
        try {
            InputStream open = getAssets().open("userdemo.xml");
            dom4jdemo.parserXml(open);
        } catch (IOException e) {
            LogUtils.e("IOException");
        } catch (DocumentException e) {
            LogUtils.e("DocumentException");
        }
    }

    public void saxXml() {
        SaxDemo saxdemo = new SaxDemo();
        try {
            InputStream open = getAssets().open("userdemo.xml");
            saxdemo.parserXml(open);
        } catch (IOException e) {
            LogUtils.e("IOException");
        } catch (ParserConfigurationException e) {
            LogUtils.e("ParserConfigurationException");
        } catch (SAXException e) {
            LogUtils.e("SAXException");
        }
    }
}
