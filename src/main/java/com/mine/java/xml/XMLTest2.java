package com.mine.java.xml;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CaoY
 * @date 2023-06-28 17:32
 * @description XML学习 - XML 验证
 * 下面代码主要功能是解析 XML 文件并将其转换为配置信息（config）。它通过使用 Java 的 DOM 解析库，
 * 读取指定的 XML 文件，并将其转换为一个包含配置信息的 Map 对象。
 *
 * 解析 XML 文档时使用 DTD模式或 Schema 模式验证的原因：
 *  1）结构验证：通过DTD或Schema模式，可以定义XML文档的结构，包括元素、属性、嵌套关系和顺序等。
 *  在解析XML文件时，验证可以确保XML文档与定义的结构相匹配，从而捕获并报告任何不符合结构的错误。
 *  2）数据类型验证：DTD或Schema模式还可以定义元素和属性的数据类型，如字符串、整数、布尔值等。
 *  通过验证，可以确保XML文档中的数据类型与定义的类型相匹配，从而防止数据类型错误。
 *  3）格式验证：验证还可用于检查XML文档中的数据格式是否合法。例如，可以使用正则表达式模式定义
 *  一个元素或属性的有效格式，并在解析过程中验证其正确性。
 *  4）文档约束和规则：通过DTD或Schema模式，可以约束XML文档中元素和属性的出现次数、顺序和其他规则。
 *  这有助于确保XML数据的完整性和一致性。
 * 通过使用验证，可以在解析XML文件之前进行检查，从而及早地发现和处理潜在的错误。
 * 这有助于提高代码的健壮性和可靠性，并确保处理的数据符合预期的格式和规范。
 */
public class XMLTest2 {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, ReflectiveOperationException {
        // 解析的 XML 文件 test5.xml
        String fileName = "src" + File.separator + "main" + File.separator +
                "resources" + File.separator + "xml" + File.separator + "test5.xml";

        // 创建一个 DocumentBuilderFactory 实例，并设置验证模式和空白忽略选项。
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);

        // boolean useSchema = true;// useSchema 为true时使用 Schema 模式，但是不能正确运行
        boolean useSchema = false;

        if (useSchema) {
            factory.setNamespaceAware(true);
            final String JAXP_SCHEMA_LANGUAGE =
                    "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
            final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
            factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
        }

        // 消除元素内容中的空白
        factory.setIgnoringElementContentWhitespace(true);

        DocumentBuilder builder = factory.newDocumentBuilder();

        // 设置 DocumentBuilder 的 ErrorHandler，用于处理解析过程中的错误。
        builder.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(SAXParseException exception) throws SAXException {
                System.err.println("Warning: " + exception.getMessage());
            }

            @Override
            public void error(SAXParseException exception) throws SAXException {
                System.err.println("Error: " + exception.getMessage());
                System.exit(0);
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                System.err.println("Fatal error: " + exception.getMessage());
                System.exit(0);
            }
        });

        Document doc = builder.parse(fileName);
        // 调用 parseConfig 方法，将 Document 对象转换为配置信息的 Map 对象。
        Map<String, Object> config = parseConfig(doc.getDocumentElement());
        System.out.println(config);
    }

    // 遍历 XML 文件中的子元素，解析每个子元素并将其存储在结果 Map 中
    private static Map<String, Object> parseConfig(Element e)
            throws ReflectiveOperationException {
        HashMap<String, Object> result = new HashMap<>();
        NodeList children = e.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Element child = (Element) children.item(i);
            String name = child.getAttribute("id");
            Object value = parseObject((Element) child.getFirstChild());
            result.put(name, value);
        }
        return result;
    }

    // 根据元素标签名的不同，将对应的值解析为不同的数据类型（如 int、boolean 或字符串）。
    // 在 parseFactory 和 parseConstruct 方法中，根据 XML 中的 class 和 method 属性，
    // 使用反射机制实例化对象或调用静态方法。
    private static Object parseObject(Element e)
            throws ReflectiveOperationException {
        String tagName = e.getTagName();
        if ("factory".equals(tagName)) {
            return parseFactory(e);
        } else if ("construct".equals(tagName)) {
            return parseConstruct(e);
        } else {
            String childData = ((CharacterData) e.getFirstChild()).getData();
            if ("int".equals(tagName)) {
                return Integer.valueOf(childData);
            } else if ("boolean".equals(tagName)) {
                return Boolean.valueOf(childData);
            } else {
                return childData;
            }
        }
    }

    private static Object parseFactory(Element e)
            throws ReflectiveOperationException {
        String className = e.getAttribute("class");
        String methodName = e.getAttribute("method");
        Object[] args = parseArgs(e.getChildNodes());
        Class<?>[] parameterTypes = getParameterTypes(args);
        Method method = Class.forName(className).getMethod(methodName, parameterTypes);
        return method.invoke(null, args);
    }

    private static Object parseConstruct(Element e)
            throws ReflectiveOperationException {
        String className = e.getAttribute("class");
        Object[] args = parseArgs(e.getChildNodes());
        Class<?>[] parameterTypes = getParameterTypes(args);
        Constructor<?> constructor = Class.forName(className).getConstructor(parameterTypes);
        return constructor.newInstance(args);
    }

    private static Object[] parseArgs(NodeList elements) throws ReflectiveOperationException {
        Object[] result = new Object[elements.getLength()];
        for (int i = 0; i < result.length; i++) {
            result[i] = parseObject((Element)elements.item(i));
        }
        return result;
    }

    private static Map<Class<?>, Class<?> > toPrimitive = new HashMap<>();
    static {
        toPrimitive.put(Integer.class, int.class);
        toPrimitive.put(Boolean.class, boolean.class);
    }

    private static Class<?>[] getParameterTypes(Object[] args) {
        Class<?>[] result = new Class<?>[args.length];
        for (int i = 0; i < result.length; i++) {
            Class<?> cl = args[i].getClass();
            result[i] = toPrimitive.get(cl);
            if (result[i] == null) {
                result[i] = cl;
            }
        }
        return result;
    }
}
