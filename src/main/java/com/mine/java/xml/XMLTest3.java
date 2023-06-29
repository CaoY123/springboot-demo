package com.mine.java.xml;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author CaoY
 * @date 2023-06-29 15:54
 * @description XML 学习 - 使用 XPath 来定位信息
 * 1. XPath（XML Path Language）是一种用于在XML文档中定位和选择节点的语言。
 * 它是W3C制定的标准，并作为XML的核心技术之一。
 * 2. XPath使用一种基于路径表达式的语法来描述XML文档的结构和层次关系。通过XPath，
 * 可以以一种简洁而直观的方式指定所需的节点，无论是根据节点的名称、属性、位置还是其他条件。
 * 3. XPath的一些基本概念：
 *  1）节点：XML文档可以包含不同类型的节点，如元素节点、属性节点、文本节点、注释节点等。
 *  在XPath中，节点是基本的数据单元。
 *  2）路径表达式：XPath使用路径表达式来定位和选择节点。路径表达式可以描述节点的层次关系和位置。
 *  例如，/bookstore/book/title表示选择根元素bookstore下的book元素的title子元素。
 *  3）节点关系：XPath支持不同节点之间的关系，如父子关系、兄弟关系、祖先关系、后代关系等。
 *  例如，//book表示选择文档中所有名为book的元素。
 *  4）谓词：谓词是XPath中的条件筛选器，用于进一步限制节点的选择范围。它可以根据节点的属性、值、位置等条件进行过滤。
 *  例如，//book[@category='fiction']表示选择文档中所有category属性值为fiction的book元素。
 *  5）内置函数：XPath提供了一些内置函数，用于处理和操作节点的值。
 *  例如，count()函数用于计算节点集的数量，text()函数用于获取节点的文本内容。
 *
 *  下面场景使用 XPath 定位并解析 test6.xml 中的内容，通过不同的 expression 练习了其基本使用。
 *
 * 4. 一般的使用 XPath 解析 XML 文档的流程：
 *  4.1 创建解析器和XPath对象：
 *      1）使用DocumentBuilderFactory.newInstance()创建DocumentBuilderFactory实例，用于创建XML解析器。
 *      2）使用DocumentBuilder从解析器工厂中创建DocumentBuilder实例，用于解析XML文档。
 *      3）使用XPathFactory.newInstance()创建XPathFactory实例，用于创建XPath对象。
 *      4）使用XPath接口的compile()方法将XPath表达式编译为XPathExpression对象。
 *  4.2 解析XML文档：
 *      1）使用DocumentBuilder.parse()方法解析XML文档，生成一个表示整个XML文档的Document对象。
 *  4.3 定义XPath表达式：
 *      1）定义一个字符串变量，存储要使用的XPath表达式。
 *  4.4 评估XPath表达式：
 *      1）使用XPathExpression.evaluate()方法对XML文档进行XPath表达式的评估。
 *      2）传递要评估的XML文档和期望的结果类型（如XPathConstants.NODESET、XPathConstants.STRING等）作为参数。
 *  4.5 处理结果：
 *      1）根据评估的结果类型，对结果进行处理。
 *          # 如果结果是节点集（NodeSet），可以使用NodeList来迭代和处理每个节点。
 *          # 如果结果是单个节点（Node），可以直接对节点进行操作。
 *          # 如果结果是字符串（String）、数值（Number）等其他类型，可以直接使用结果。
 *  4.6 清理资源：
 *      1）在完成XPath解析后，应关闭文件或释放其他资源。
 */
public class XMLTest3 {
    public static void main(String[] args)
            throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        XPathFactory xpFactory = XPathFactory.newInstance();
        XPath path = xpFactory.newXPath();

        String fileName = "src" + File.separator + "main" + File.separator +
                "resources" + File.separator + "xml" + File.separator + "test6.xml";

        Document doc = builder.parse(fileName);

        String expression = "/catalog/public";
//        String expression = "/catalog/public/@publicId";
//        String expression = "/catalog/public[1]/@publicId";// 下标从1开始
//        String expression = "count(/catalog/public)";// 下标从1开始

        XPathExpression expr = path.compile(expression);

        // 设置命名空间映射
        NamespaceContext nsContext = new NamespaceContext() {
            @Override
            public String getNamespaceURI(String prefix) {
                if ("xml".equals(prefix)) {
                    return "urn:oasis:names:tc:entity:xmlns:xml:catalog";
                }
                return null;
            }

            @Override
            public String getPrefix(String namespaceURI) {
                return null;
            }

            @Override
            public Iterator<String> getPrefixes(String namespaceURI) {
                return null;
            }
        };
        path.setNamespaceContext(nsContext);

        // 评估XPath表达式
        Object result = expr.evaluate(doc, XPathConstants.NODESET);// 不使用内置函数 count() 时
//        Object result = expr.evaluate(doc, XPathConstants.NUMBER);// 使用内置函数 count() 时

        // 处理结果
        if (result instanceof NodeList) {
            NodeList nodeList = (NodeList) result;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                System.out.println(description(node));
            }
        } else if (result instanceof Node) {
            Node node = (Node) result;
            System.out.println(node);
        } else {
            System.out.println(result);
        }

    }

    private static String description(Node n) {
        if (n instanceof Element) {
            return "Element " + n.getNodeName();
        } else if (n instanceof Attr) {
            return "Attribute " + n;
        } else {
            return n.toString();
        }
    }
}
