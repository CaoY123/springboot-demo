package com.mine.java.xml;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


/**
 * @author CaoY
 * @date 2023-07-01 15:20
 * @description XML 学习 - XSL 转换
 * 1. XSL 转换知识：
 *  1）XSLT语法：XSLT（Extensible Stylesheet Language Transformations）是一种基于XML的语言，
 *  用于定义XML文档的转换规则。熟悉XSLT的语法和基本概念对于编写有效的转换样式表至关重要。
 *  2）样式表结构：XSLT样式表由模板（template）、匹配模式（match pattern）、选择器（selector）、
 *  XPath表达式等组成。通过这些元素和规则，您可以指定如何将源XML文档转换为目标格式。
 *  3）XPath表达式：XPath是一种用于定位和选择XML文档中节点的语言。您可以使用XPath表达式来指定哪些节点被选中、
 *  复制或转换成其他形式。了解XPath的基本语法和功能将有助于更好地编写XSLT样式表。
 *  4）模板匹配：模板匹配是指指定哪些节点会应用特定的转换规则。通过使用匹配模式，您可以根据节点名称、属性、
 *  路径等条件来匹配要转换的节点。请确保样式表的匹配模式能够准确地选择目标节点。
 *  5）转换器和引擎：在Java中，可以使用Transformer类来执行XSLT转换。通过TransformerFactory实例获取转换器，
 *  并加载样式表。然后，通过调用transform()方法指定输入源和输出源，执行实际的转换操作。
 *  6）输出格式：转换的结果可以是XML、HTML或文本等多种格式。根据您的需求，可以指定输出格式的设置，
 *  例如缩进、编码、DOCTYPE声明等。
 *  7）调试和测试：在进行复杂的转换过程时，可能需要进行调试和测试以确保样式表正确地执行转换操作。
 *  可以使用调试工具、打印中间结果或针对特定节点进行单元测试来帮助排除问题。
 */
public class XMLTest7 {

    /**
     * 普通转换
     * 将 employee.dat 中的内容解析成 XML 文档格式的，再将 XML 文档格式的解析成 HTML 表格格式的内容。
     */
    @Test
    public void test1() {
        try {
            // 创建 DocumentBuilder 实例
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // 解析 employee.dat 文件
            File file = new File("src" + File.separator + "main" + File.separator +
                    "resources" + File.separator + "xml" + File.separator + "employee.dat");
            Document doc = builder.newDocument();

            // 创建根元素 <staff>
            Element staffElement = doc.createElement("staff");
            doc.appendChild(staffElement);

            // 读取数据并创建相应的 XML 元素
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split("\\|");

                // 创建 <employee> 元素
                Element employeeElement = doc.createElement("employee");

                // 创建 <name> 元素并设置文本内容
                Element nameElement = doc.createElement("name");
                nameElement.setTextContent(data[0]);
                employeeElement.appendChild(nameElement);

                // 创建 <salary> 元素并设置文本内容
                Element salaryElement = doc.createElement("salary");
                salaryElement.setTextContent(data[1]);
                employeeElement.appendChild(salaryElement);

                // 创建 <hiredate> 元素并设置属性值
                Element hiredateElement = doc.createElement("hiredate");
                hiredateElement.setAttribute("year", data[2]);
                hiredateElement.setAttribute("month", data[3]);
                hiredateElement.setAttribute("day", data[4]);
                employeeElement.appendChild(hiredateElement);

                staffElement.appendChild(employeeElement);
            }
            scanner.close();

            // 将生成的 XML 文档保存
            String xmlPath = "src" + File.separator + "main" + File.separator +
                    "resources" + File.separator + "xml" + File.separator + "test11.xml";
            writeDocument(doc, xmlPath);

            // 在控制台打印生成的 XML 文档
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(System.out);
            transformer.transform(source, result);

            // 使用转换器将 XML 文档和样式表进行转换
            Transformer xsltTransformer = transformerFactory.newTransformer(new StreamSource("src" + File.separator + "main" + File.separator +
                    "resources" + File.separator + "xml" + File.separator + "makehtml.xsl"));
            Source xmlSource = new DOMSource(doc);
            Result output = new StreamResult(new File("src" + File.separator + "main" + File.separator +
                    "resources" + File.separator + "xml" + File.separator + "employee.html"));
            xsltTransformer.transform(xmlSource, output);

            System.out.println("=====================转换完成=====================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于将XML文档对象写入到文件中
     * @param doc       写入的Document对象
     * @param fileName  文件路径名
     * @throws TransformerException
     * @throws IOException
     */
    public static void writeDocument(Document doc, String fileName)
            throws TransformerException, IOException {
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
                "http://www.w3.org/TR/2000/CR-SVG-20000802/DTD/svg-20000802.dtd");
        t.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,
                "-//W3C//DTD SVG 20000802//EN");
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty(OutputKeys.METHOD, "xml");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        t.transform(new DOMSource(doc), new StreamResult(Files.newOutputStream(Paths.get(fileName))));
    }

    /**
     * XSL 转换，使用的是 test1 中生成的 test11.xml 文档，转换为 HTML 格式的文档
     */
    @Test
    public void test2() throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        // 创建转换器并加载样式表
        Transformer transformer = transformerFactory.newTransformer(new StreamSource("src" + File.separator + "main" + File.separator +
                "resources" + File.separator + "xml" + File.separator + "makehtml.xsl"));

        String xmlSourcePath = "src" + File.separator + "main" + File.separator +
                "resources" + File.separator + "xml" + File.separator + "test11.xml";

        String htmlTargetPath = "src" + File.separator + "main" + File.separator +
                "resources" + File.separator + "xml" + File.separator + "test11.html";

        // 执行转换
        transformer.transform(new StreamSource(xmlSourcePath), new StreamResult(htmlTargetPath));

        System.out.println("=====================转换完成=====================");
    }
}
