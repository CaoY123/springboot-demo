package com.mine.java.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * @author CaoY
 * @date 2023-06-29 17:27
 * @description XML 学习 - 使用命名空间
 * 1. 在XML中引入命名空间的主要目的是解决不同XML文档或不同XML元素之间的名称冲突。
 * 当多个XML文档或元素共存时，可能存在相同的元素或属性名称，这会导致歧义和混淆。
 * 为了避免这种冲突，引入命名空间来给XML元素和属性添加唯一的标识。
 *
 * 2. 下面是一些使用命名空间的原因：
 *  1）唯一性：命名空间确保在同一个XML文档中不同的模块或作者可以使用相同的元素或属性名称，而不会发生冲突。
 *  命名空间通过命名空间URI来定义一个独特的标识符，以区分不同的元素或属性。
 *  2）语义清晰：命名空间可以提供上下文和语义信息。通过引入命名空间，我们可以使XML文档更加可读和易于理解。
 *  命名空间URI通常与某个特定的XML模式或应用程序相关联，这样可以为元素和属性提供更准确的含义。
 *  3）模块化和扩展性：命名空间将XML文档划分为各个独立的模块或命名空间。
 *  这种模块化的设计使得XML文档可以轻松地扩展和修改，而无需影响其他模块的结构和内容。
 *  通过引入新的命名空间URI，可以在不破坏现有结构的情况下添加新的元素和属性。
 *  4）与其他标准的互操作性：命名空间在实现与其他标准和技术的互操作性方面起到关键作用。
 *  例如，当XML与命名空间集成到Web服务、SOAP、XML Schema等技术中时，能够更好地进行数据交换和集成。
 *
 * 3. XML文档的命名空间包含以下几个重要的组成部分：
 *  1）命名空间前缀（Prefix）：命名空间前缀是一个短字符串，用于表示与命名空间URI相关联的元素和属性。
 *  通常，前缀在XML文档中通过“前缀:元素名”或“前缀:属性名”的形式使用。
 *  2）命名空间URI（Namespace URI）：命名空间URI是一个唯一标识符，用于给XML元素和属性分配独特的命名空间。
 *  它是一个字符串值，通常是一个URL格式的地址，但实际上可以是任何字符串。
 *  3）命名空间声明（Namespace Declaration）：命名空间声明在XML文档中显式地将命名空间前缀与命名空间URI关联起来。
 *  声明通常出现在XML文档的根元素上，并采用“xmlns:前缀=命名空间URI”的形式。
 */
public class XMLTest4 {

    public static void main(String[] args) throws Exception {
        // 创建解析器工厂和解析器对象
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);// 启用命名空间感知模式，必须设置，否则无法获得相应的元素和属性
        DocumentBuilder builder = factory.newDocumentBuilder();

        String xmlPath = "src" + File.separator + "main" + File.separator +
                "resources" + File.separator + "xml" + File.separator + "test7.xml";

        // 解析XML文件
        Document document = builder.parse(xmlPath);

        // 获取根元素
        Element root = document.getDocumentElement();

        // 定义命名空间
        String namespaceURI = "http://www.example.com/books";

        // 获取book元素列表，获取的时候不需要夹前缀
        NodeList bookList = root.getElementsByTagNameNS(namespaceURI, "book");

        // 遍历book元素
        for (int i = 0; i < bookList.getLength(); i++) {
            Element book = (Element) bookList.item(i);
            Element title = (Element) book.getElementsByTagNameNS(namespaceURI, "title").item(0);
            Element author = (Element) book.getElementsByTagNameNS(namespaceURI, "author").item(0);

            // 获取书籍信息
            String bookTitle = title.getTextContent();
            String bookAuthor = author.getTextContent();

            // 打印书籍信息
            System.out.println("Title: " + bookTitle);
            System.out.println("Author: " + bookAuthor);
            System.out.println("-----------------------");
        }
    }
}
