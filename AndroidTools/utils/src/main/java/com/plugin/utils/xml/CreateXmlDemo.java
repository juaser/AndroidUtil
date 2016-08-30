package com.plugin.utils.xml;

import android.util.Xml;

import com.plugin.utils.log.LogUtils;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;

/**
 * @Description: 生成一个xml
 * @Author: zxl
 * @Date: 30/8/16.
 */
public class CreateXmlDemo {
    private XmlSerializer serializer = Xml.newSerializer();
    private StringWriter writer = null;

    /**
     * startTag (String namespace, String name)这里的namespace用于唯一标识xml标签
     * XML 命名空间属性被放置于某个元素的开始标签之中，并使用以下的语法：
     * xmlns:namespace-prefix="namespaceURI"
     * 当一个命名空间被定义在某个元素的开始标签中时，所有带有相同前缀的子元素都会与同一个命名空间相关联。
     * 注释：用于标示命名空间的地址不会被解析器用于查找信息。其惟一的作用是赋予命名空间一个惟一的名称。不过，很多公司常常会作为指针来使用命名空间指向某个实存的网页，这个网页包含着有关命名空间的信息。
     * attribute(s,s1,s2) 标签属性
     */
    public void createXml() throws IOException {
        writer = new StringWriter();
        serializer.setOutput(writer);
        serializer.startDocument("UTF-8", true);//开始创建xml
        serializer.startTag(null, "users");//开始标签
        serializer.text("\n");
        for (int i = 0; i < 3; i++) {
            serializer.startTag(null, "user");
            serializer.attribute(null, "id", i + "");
            serializer.text("\n");

            serializer.startTag(null, "name");
            serializer.text("zxl" + i);
            serializer.endTag(null, "name");
            serializer.text("\n");

            serializer.startTag(null, "age");
            serializer.text("age" + i);
            serializer.endTag(null, "age");
            serializer.text("\n");

            serializer.startTag(null, "sex");
            serializer.text("sex" + i);
            serializer.endTag(null, "sex");
            serializer.text("\n");

            serializer.endTag(null, "user");
        }
        serializer.endTag(null, "users");//结束标签
        serializer.endDocument();//结束文档编辑
        LogUtils.e("编辑的xml==" + writer.toString());

    }

//    <users>
//        <user id="0">
//               <name>Alexia</name>
//               <age>23</age>
//               <sex>Female</sex>
//        </user>
//        <user id="1">
//              <name>Edward</name>
//              <age>24</age>
//              <sex>Male</sex>
//        </user>
//        <user id="2">
//             <name>wjm</name>
//             <age>23</age>
//             <sex>Female</sex>
//       </user>
//       <user id="3">
//             <name>wh</name>
//             <age>24</age>
//            <sex>Male</sex>
//        </user>
//    </users>
}
