package com.mine.java.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

/**
 * @author CaoY
 * @date 2023-07-01 12:54
 * @description XML 学习 - 生成 XML 文档
 * 使用代码生成 XML 文档，两个方式：
 * 1. 通过构建并保存 DOM 树构建XML文档
 * 2. 通过直接用 StAX API 写出 XML 文档
 *
 * 下面的代码主要是创建一个SVG绘图，并将其写入到两个不同的XML文件中。
 */
public class XMLTest6 {

    public static void main(String[] args)
            throws Exception {
        Document doc = newDrawing(600, 400);
        String xmlFilePath1 = "src" + File.separator + "main" + File.separator +
                "resources" + File.separator + "xml" + File.separator + "test9.svg";
        writeDocument(doc, xmlFilePath1);

        String xmlFilePath2 = "src" + File.separator + "main" + File.separator +
                "resources" + File.separator + "xml" + File.separator + "test10.svg";
        writeNewDrawing(600, 400, xmlFilePath2);
        // 生成的是一个不同颜色矩形块构成的图片，但是本质上也是一个类似于 XML 文件的标签和值，可以用notepad看到
    }

    private static Random generator = new Random(System.currentTimeMillis());

    /**
     * 通过构建并保存 DOM 树构建XML文档
     *
     * 使用了Java的DocumentBuilder和Document对象来创建一个空的XML文档。
     * 然后使用命名空间和元素的方式构建了SVG根元素，并设置了绘图的宽度和高度。
     * 接下来，使用一个循环生成了一定数量的矩形元素，每个矩形的位置、大小和颜色都是随机生成的。
     * 最后，返回生成的XML文档对象。
     * @param drawingWidth  绘图宽度
     * @param drawingHeight 灰度高度
     * @return
     * @throws ParserConfigurationException
     */
    public static Document newDrawing(int drawingWidth, int drawingHeight)
            throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();

        String namespace = "http://www.w3.org/2000/svg";
        Document doc = builder.newDocument();
        Element svgElement = doc.createElementNS(namespace, "svg");

        doc.appendChild(svgElement);
        svgElement.setAttribute("width", "" + drawingWidth);
        svgElement.setAttribute("height", "" + drawingHeight);

        int n = 10 + generator.nextInt(20);
        for (int i = 0; i <= n; i++) {
            int x = generator.nextInt(drawingWidth);
            int y = generator.nextInt(drawingHeight);
            int width = generator.nextInt(drawingWidth - x);
            int height = generator.nextInt(drawingHeight - y);
            int r = generator.nextInt(256);
            int g = generator.nextInt(256);
            int b = generator.nextInt(256);

            Element rectElement = doc.createElementNS(namespace, "rect");
            rectElement.setAttribute("x", "" + x);
            rectElement.setAttribute("y", "" + y);
            rectElement.setAttribute("width", "" + width);
            rectElement.setAttribute("height", "" + height);
            rectElement.setAttribute("fill",
                    String.format("#%02x%02x%02x", r, g, b));
            svgElement.appendChild(rectElement);
        }

        return doc;
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
     * 通过直接用 StAX API 写出 XML 文档
     *
     * 用于将SVG绘图写入到文件中，不同的是它使用了XMLStreamWriter来实现。
     * 使用XMLOutputFactory和XMLStreamWriter对象创建一个XML写入器，并设置了绘图的宽度和高度。
     * 然后，使用一个循环生成了一定数量的矩形元素，并设置其位置、大小和颜色。
     * 最后，调用writeEndDocument方法关闭根元素，并将结果写入到指定的文件中。
     * @param drawingWidth  绘图宽度
     * @param drawingHeight 绘图高度
     * @param fileName      文件路径
     * @throws IOException
     * @throws XMLStreamException
     */
    public static void writeNewDrawing(int drawingWidth, int drawingHeight, String fileName)
            throws IOException, XMLStreamException {
        XMLOutputFactory factory = XMLOutputFactory.newFactory();
        XMLStreamWriter writer = factory.createXMLStreamWriter(
                Files.newOutputStream(Paths.get(fileName)));

        writer.writeStartDocument();
        writer.writeDTD("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 20000802//EN\"\n" +
                "\"http://www.w3.org/TR/2000/CR-SVG-20000802/DTD/svg-20000802.dtd\">");
        writer.writeStartElement("svg");
        writer.writeDefaultNamespace("http://www.w3.org/2000/svg");
        writer.writeAttribute("width", "" + drawingWidth);
        writer.writeAttribute("height", "" + drawingHeight);
        int n = 10 + generator.nextInt(20);
        for (int i = 0; i < n; i++) {
            int x = generator.nextInt(drawingWidth);
            int y = generator.nextInt(drawingHeight);
            int width = generator.nextInt(drawingWidth - x);
            int height = generator.nextInt(drawingHeight - y);
            int r = generator.nextInt(256);
            int g = generator.nextInt(256);
            int b = generator.nextInt(256);
            writer.writeEmptyElement("rect");
            writer.writeAttribute("x", "" + x);
            writer.writeAttribute("y", "" + y);
            writer.writeAttribute("width", "" + width);
            writer.writeAttribute("height", "" + height);
            writer.writeAttribute("fill", String.format("#%02x%02x%02x", r, g, b));
        }
        writer.writeEndDocument();// close svg element
    }
}
