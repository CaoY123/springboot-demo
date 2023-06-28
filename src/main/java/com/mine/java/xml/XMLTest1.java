package com.mine.java.xml;

import org.junit.Test;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author CaoY
 * @date 2023-06-28 1:24
 * @description XML学习 - XML 初步认识及基础解析的
 *
 * 1. XML 的几条规则：
 *  1）大小写敏感；
 *  2）XML中如果没有结束标签，则单个标签需要以 / 结尾，如：<img src='a.jpg' />，否则结束标签不能省略；
 *  3）XML中属性值必须用双引号引起来，如：<apple height="300"><apple/> 中的300必须加引号；
 *  4）在XML中，所有属性必须都有属性值，即不允许出现：<a checked><a/> 这种情况，而是 <a checked="true"><a/> 。
 *
 * 2. 关于 XML 中的属性和元素的使用：
 *  1） 一条常用的经验是：属性只应该用来修改值的解释，而不是用来指定值，如：
 *  <font>
 *      <name>Helvetica<name/>
 *      <size unit="pt">36</size>
 *  <font/>
 *  而如果陷入了这种纠结，即是该使用元素还是使用属性描述信息，一般倾向于使用元素而非属性。
 *
 *  下面的练习是一个简单 XML 文件的基本解析，调用了一些基础的 API 完成的解析。
 */
public class XMLTest1 {

    // 单纯打印一下XML文档，看看能否成功关联到
    @Test
    public void test1() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        FileInputStream fis = new FileInputStream("src" + File.separator + "main" +
                File.separator + "resources" + File.separator + "xml" + File.separator +
                "test1.xml");
        // 下面 Document 对象 doc 表示整个 XML文档
        Document doc = builder.parse(fis);

        // 将 Document 对象转换为字符串并输出到控制台
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        // Transformer 对象可以将 XML数据转换为不同格式
        Transformer transformer = transformerFactory.newTransformer();
        // domSource 为被转换的源数据
        DOMSource domSource = new DOMSource(doc);
        // 将 StreamResult 与系统输出流关联，StreamResult 表示将转换结果输出到指定的输出流
        StreamResult streamResult = new StreamResult(System.out);
        // 使用 Transformer 对象 transformer 将 domSource 中的 XML 数据转换，
        // 并将结果输出到 streamResult 对应的输出流中，这里是系统输出流。
        transformer.transform(domSource, streamResult);
    }

    // 分析一下 XML 文档的内容，解析一下其中的元素、文档以及属性。
    @Test
    public void test2() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        FileInputStream fis = new FileInputStream("src" + File.separator + "main" +
                File.separator + "resources" + File.separator + "xml" + File.separator +
                "test1.xml");
        Document doc = builder.parse(fis);

        // getDocumentElement() 启动对文档内容的分析
        Element root = doc.getDocumentElement();
        System.out.println(root);
        System.out.println("标签名：" + root.getTagName());

        // 获得该元素的子元素
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i); // 得到元素中的所有内容，包括一些空白字符
            if (child instanceof Element) {// 只处理属于元素的部分
                Element childElement = (Element) child;
                // trim() 可以剔除掉文本中的空白字符
                System.out.println("子标签名：" + childElement.getTagName().trim() +
                        "，值：" + childElement.getTextContent().trim());

                if (childElement.hasAttributes()) {
                    // 解析子元素中的属性
                    NamedNodeMap attributes = childElement.getAttributes();
                    for (int j = 0; j < attributes.getLength(); j++) {
                        Node attribute = attributes.item(j);
                        System.out.println("属性名：" + attribute.getNodeName() +
                                "，属性值：" + attribute.getNodeValue());
                    }
                }
                System.out.println("===============================");
            }
        }
    }

    @Test
    public void test3() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        FileInputStream fis = new FileInputStream("src" + File.separator + "main" +
                File.separator + "resources" + File.separator + "xml" + File.separator +
                "test1.xml");
        Document doc = builder.parse(fis);

        Element root = doc.getDocumentElement();
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child instanceof Element) {
                // 使用一下 getFirstChild() 和 getLastChild()
                Element childElement = (Element) child;
                Text firstChildText = (Text) childElement.getFirstChild();
                String firstText = firstChildText.getData().trim();
                System.out.println(firstText);

                Text lastChildText = (Text) childElement.getLastChild();
                String lastText = lastChildText.getData().trim();
                System.out.println(lastText);
            }
        }
    }

    // 探究前后的兄弟结点以及父结点
    @Test
    public void test4() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        FileInputStream fis = new FileInputStream("src" + File.separator + "main" +
                File.separator + "resources" + File.separator + "xml" + File.separator +
                "test1.xml");
        Document doc = builder.parse(fis);

        Element root = doc.getDocumentElement();
        // 使用 getNextSibling() 获取兄弟结点的方式遍历子结点
        for (Node childNode = root.getFirstChild(); childNode != null;
             childNode = childNode.getNextSibling()) {
            if (childNode instanceof Element) {
                System.out.println("=============元素结点============");
                Element childElement = (Element) childNode;
                System.out.println(childElement.getTagName());
                System.out.println("===============================");
            } else {
                System.out.println("当前结点：" + childNode.getNodeName());
                // 获取一下上一个结点和父节点
                Node previousNode = childNode.getPreviousSibling();
                System.out.println("上一个结点：" + (previousNode == null ? "为空" : previousNode.getNodeName()));
                Node parentNode = childNode.getParentNode();
                System.out.println("父结点：" + (parentNode == null ? "为空" : parentNode.getNodeName()));
            }
        }
    }
}
