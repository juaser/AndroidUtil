package com.zxl.androidtools.ui;

import com.plugin.utils.IOUtils;
import com.plugin.utils.base.BaseAppCompatActivity;
import com.plugin.utils.log.LogUtils;
import com.plugin.utils.xml.CreateXmlDemo;
import com.plugin.utils.xml.Dom4jDemo;
import com.plugin.utils.xml.DomDemo;
import com.plugin.utils.xml.JDomDemo;
import com.plugin.utils.xml.PullDemo;
import com.plugin.utils.xml.SaxDemo;
import com.zxl.androidtools.R;

import org.dom4j.DocumentException;
import org.jdom2.JDOMException;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import butterknife.OnClick;

/**
 * @Description:
 * @Author: zxl
 * @Date: 30/8/16.
 */
public class TestXmlActivity extends BaseAppCompatActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_parserxml;
    }

    @Override
    public void initView() {

    }

    @OnClick(R.id.tv_domparser)
    void domXml() {
        InputStream open = null;
        try {
            open = getAssets().open("userdemo.xml");
            DomDemo.getInstance().parserXml(open);
        } catch (IOException e) {
            LogUtils.e("IOException");
        } catch (ParserConfigurationException e) {
            LogUtils.e("ParserConfigurationException");
        } catch (SAXException e) {
            LogUtils.e("SAXException");
        } finally {
            IOUtils.close(open);
        }
    }

    @OnClick(R.id.tv_jdom)
    void jdomXml() {
        InputStream open = null;
        try {
            open = getAssets().open("userdemo.xml");
            JDomDemo.getInstance().parserXml(open);
        } catch (IOException e) {
            LogUtils.e("IOException");
        } catch (JDOMException e) {
            LogUtils.e("JDOMException");
        } finally {
            IOUtils.close(open);
        }
    }

    @OnClick(R.id.tv_dom4j)
    void dom4jXml() {
        InputStream open = null;
        try {
            open = getAssets().open("userdemo.xml");
            Dom4jDemo.getInstance().parserXml(open);
        } catch (IOException e) {
            LogUtils.e("IOException");
        } catch (DocumentException e) {
            LogUtils.e("DocumentException");
        } finally {
            IOUtils.close(open);
        }
    }

    @OnClick(R.id.tv_sax)
    void saxXml() {
        InputStream open = null;
        try {
            open = getAssets().open("userdemo.xml");
            SaxDemo.getInstance().parserXml(open);
        } catch (IOException e) {
            LogUtils.e("IOException");
        } catch (ParserConfigurationException e) {
            LogUtils.e("ParserConfigurationException");
        } catch (SAXException e) {
            LogUtils.e("SAXException");
        } finally {
            IOUtils.close(open);
        }
    }

    @OnClick(R.id.tv_pull)
    void pullXml() {
        InputStream open = null;
        try {
            open = getAssets().open("userdemo.xml");
            PullDemo.getInstance().parserXml(open);
        } catch (IOException e) {
            LogUtils.e("IOException");
        } catch (XmlPullParserException e) {
            LogUtils.e("XmlPullParserException");
        } finally {
            IOUtils.close(open);
        }
    }

    @OnClick(R.id.tv_createxml)
    void create() {
        try {
            CreateXmlDemo.getInstance().createXml();
        } catch (IOException e) {
            LogUtils.e("IOException");
        }
    }
}
