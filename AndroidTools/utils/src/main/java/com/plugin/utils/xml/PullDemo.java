package com.plugin.utils.xml;

import android.text.TextUtils;
import android.util.Xml;

import com.plugin.utils.log.LogUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Description: pull解析xml文档¬
 * @Author: zxl
 * @Date: 30/8/16.
 */
public class PullDemo {
    private XmlPullParser pullParser = Xml.newPullParser();
    private static volatile PullDemo mInstance = null;

    private PullDemo() {
    }

    public static PullDemo getInstance() {
        PullDemo instance = mInstance;
        if (instance == null) {
            synchronized (PullDemo.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new PullDemo();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    public void parserXml(InputStream is) throws XmlPullParserException, IOException {
        pullParser.setInput(is, "UTF-8");//为Pull解析器设置要解析的XML数据
        int event = pullParser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:
                    //标签处开始
                    LogUtils.e("XmlPullParser------start");
                    break;

                case XmlPullParser.START_TAG:
                    //此标签属性 一共有几个 比如 <user id="0"> 此时只有一个 count==1 pullParser.getName()是指标签名称 此标签名称为user
                    //int count = pullParser.getAttributeCount();
                    //for (int i = 0; i < count; i++) {
                    //LogUtils.e(pullParser.getAttributeName(i) + "------" + pullParser.getAttributeValue(0));//查看标签属性的名称和值
                    //}
                    if (TextUtils.equals("name", pullParser.getName()))
                        LogUtils.e("name==" + pullParser.nextText());//获取标签携带的数据
                    if (TextUtils.equals("age", pullParser.getName()))
                        LogUtils.e("age==" + pullParser.nextText());//获取标签携带的数据
                    if (TextUtils.equals("sex", pullParser.getName()))
                        LogUtils.e("sex==" + pullParser.nextText());//获取标签携带的数据
                    break;

                case XmlPullParser.END_TAG:
                    //扫描到反斜杠标签 如果此处是以users结束的，说明已经扫描到最后
                    if (TextUtils.equals("users", pullParser.getName()))
                        LogUtils.e("XmlPullParser------end");
                    break;
            }
            event = pullParser.next();
        }
    }
}
