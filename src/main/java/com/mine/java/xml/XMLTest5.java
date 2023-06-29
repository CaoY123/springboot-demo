package com.mine.java.xml;

import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author CaoY
 * @date 2023-06-29 19:41
 * @description XML 学习 - 流机制解析器
 *
 * 1. XML文档通常使用流式解析器来解析，主要有以下几个原因：
 *  1）内存效率：XML文档可能非常大，使用DOM（文档对象模型）解析会将整个文档加载到内存中，
 *  这对于大型文档会占用大量内存。相比之下，流式解析器提供了一种逐行或逐个元素解析文档的方式，
 *  只需要在解析时保持当前状态和所需的上下文信息，无需将整个文档加载到内存中，因此内存占用更低。
 *  2）解析速度：由于流式解析器只需要处理当前解析位置的数据，而不是整个文档，因此可以在解析过程中逐步读取和处理数据，
 *  这使得解析速度更快。相比之下，DOM解析器需要在内存中构建一个完整的文档树结构，可能需要花费更多的时间。
 *  3）事件驱动：流式解析器遵循事件驱动的解析模型，解析器通过触发各种事件来表示不同的解析状态，
 *  开发人员可以根据自己的需求选择性地处理这些事件。这种事件驱动的模型使得解析器具有更高的灵活性和可扩展性，
 *  可以编写处理特定事件的代码，而无需处理整个文档。
 *  4）异步处理：由于流式解析器以事件驱动的方式工作，它可以逐步处理数据，并允许异步处理。
 *  这对于需要同时解析多个大型XML文档并执行其他任务的应用程序很有用，可以提高系统的响应能力和效率。
 *
 * 2. 流式解析器（Streaming Parser）是一种逐个元素或逐行解析文档的解析器，它以事件驱动的方式处理XML文档。
 * 与DOM解析器不同，流式解析器在内存中不会构建整个文档树结构，而是在解析过程中逐步读取和处理数据。
 *
 * 3. 解析器类型：
 *  1）SAX（Simple API for XML）解析器：SAX是最常见的流式解析器之一，它通过事件触发器（Event Handler）
 *  和回调方法（Callback）来处理XML文档。当解析器遍历XML文档时，会触发各种事件（例如开始元素、结束元素、文本内容等），
 *  开发人员可以编写相应的处理代码来捕获这些事件并执行相应的操作。
 *  2）StAX（Streaming API for XML）解析器：StAX也是一种流式解析器，但相对于SAX，它提供了更简洁和易于使用的API。
 *  StAX解析器基于迭代器（Iterator）的原理，开发人员可以使用XMLStreamReader来逐步遍历XML文档，
 *  并根据不同类型的事件进行处理。此外，StAX还提供了XMLStreamWriter来生成XML文档。
 *
 * 4. 解析过程：
 *  1）解析状态维护：流式解析器需要维护当前解析的位置、状态和上下文信息。使用解析器提供的方法，
 *  开发人员可以获取当前解析位置的元素名称、属性、命名空间等，并在解析过程中保持相应的状态。
 *  2）事件驱动模型：流式解析器基于事件驱动的模型，解析器会触发各种事件来表示不同的解析状态。
 *  开发人员可以根据自己的需求选择性地处理这些事件，例如处理开始元素事件、结束元素事件、字符数据事件等。
 *
 * 5. 流式解析器不适合在解析过程中频繁访问之前或之后的数据，因为它只能以前进的方式逐步处理文档。
 * 如果需要随机访问文档的不同部分，DOM解析器可能更合适。
 */
public class XMLTest5 {

    /**
     * 使用 SAX 解析 XML 文档，当解析到元素时，会打印相应的信息，包括元素的名称、属性和内容。
     */
    @Test
    public void test1() throws ParserConfigurationException, SAXException, IOException {
        // 创建 SAXParserFactory 实例
        SAXParserFactory factory = SAXParserFactory.newInstance();

        // 创建 SAXParser 实例
        SAXParser saxParser = factory.newSAXParser();

        // 创建自定义的 DefaultHandler 实例
        MySAXHandler handler = new MySAXHandler();

        String xmlPath = "src" + File.separator + "main" + File.separator +
                "resources" + File.separator + "xml" + File.separator + "test8.xml";

        // 解析 XML 文档
        saxParser.parse(xmlPath, handler);
    }

    private static class MySAXHandler extends DefaultHandler {

        // 开始解析文档时调用
        @Override
        public void startDocument() throws SAXException {
            System.out.println("开始解析文档");
        }

        // 结束解析文档时调用
        @Override
        public void endDocument() throws SAXException {
            System.out.println("解析文档结束");
        }

        // 开始解析元素时调用
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            System.out.println("开始解析元素: " + qName);

            // 打印元素的属性
            if (attributes.getLength() > 0) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    System.out.println("属性: " + attributes.getQName(i) + " = " + attributes.getValue(i));
                }
            }
        }

        // 结束解析元素时调用
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            System.out.println("结束解析元素: " + qName);
        }

        // 解析元素内容时调用
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String content = new String(ch, start, length);

            // 去除内容中的空白字符
            content = content.trim();

            if (!content.isEmpty()) {
                System.out.println("元素内容: " + content);
            }
        }
    }

    /**
     * 使用 StAX 解析 XML 文档
     */
    @Test
    public void test2() throws IOException, XMLStreamException {
        // 创建 XMLInputFactory 实例
        XMLInputFactory factory = XMLInputFactory.newInstance();

        String xmlPath = "src" + File.separator + "main" + File.separator +
                "resources" + File.separator + "xml" + File.separator + "test8.xml";
        // 创建 XMLStreamReader 实例
        FileInputStream fileInputStream = new FileInputStream(xmlPath);
        XMLStreamReader reader = factory.createXMLStreamReader(fileInputStream);

        // 开始解析文档
        while (reader.hasNext()) {
            int eventType = reader.next();

            // 处理不同类型的事件
            switch (eventType) {
                case XMLStreamConstants.START_DOCUMENT:
                    System.out.println("开始解析文档");
                    break;
                case XMLStreamConstants.END_DOCUMENT:
                    System.out.println("解析文档结束");
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    System.out.println("开始解析元素: " + reader.getLocalName());
                    // 打印元素的属性
                    for (int i = 0; i < reader.getAttributeCount(); i++) {
                        System.out.println("属性: " + reader.getAttributeLocalName(i) + " = " + reader.getAttributeValue(i));
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    System.out.println("结束解析元素: " + reader.getLocalName());
                    break;
                case XMLStreamConstants.CHARACTERS:
                    String content = reader.getText().trim();
                    if (!content.isEmpty()) {
                        System.out.println("元素内容: " + content);
                    }
                    break;
            }
        }

        // 关闭 XMLStreamReader 和 FileInputStream
        reader.close();
        fileInputStream.close();
    }
}
