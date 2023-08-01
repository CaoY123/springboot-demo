package com.mine.java.compile;

import org.junit.Test;

import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author CaoY
 * @date 2023-08-01 17:01
 * @description 脚本、编译与注解处理 - 编译器 API
 */
public class CompileTest2 {

    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }

    @Test
    public void test1() throws FileNotFoundException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        OutputStream outStream = new FileOutputStream("src" + File.separator +
                "main" + File.separator + "resources" + File.separator +
                "compile" + File.separator + "out.txt");
        FileOutputStream errStream = new FileOutputStream("src" + File.separator +
                "main" + File.separator + "resources" + File.separator +
                "compile" + File.separator + "err.txt");

        String sourcePath = "src" + File.separator +  "main" + File.separator +  "java" + File.separator +
                "com" + File.separator +  "mine" + File.separator +  "java" + File.separator + "compile" +
                File.separator + "CompileTest2.java";
        // 若 result 为 0，则表示编译成功
        // 编译器不会接受任何控制台的输入，所以第一个参数（输入流）传入总是为 null
        int result = compiler.run(null, outStream, errStream, sourcePath);
        System.out.println(result);
    }

    // 下面代码为 ChatGPT 设计，我最初是真的不知道该怎么写
    @Test
    public void test2() {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        // 创建 JavaFileManager
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        // 创建 DiagnosticListener 用于处理编译时的诊断信息
        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();

        String sourcePath = "src" + File.separator +  "main" + File.separator +  "java" + File.separator +
                "com" + File.separator +  "mine" + File.separator +  "java" + File.separator + "compile" +
                File.separator + "CompileTest2.java";
        try {
            // 读取 Java 文件内容
            String sourceCode = new String(Files.readAllBytes(Paths.get(sourcePath)), StandardCharsets.UTF_8);

            // 创建 CompilationTask
            JavaFileObject sourceFile = new JavaSourceFromString("CompileTest2", sourceCode);
            Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(sourceFile);
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnosticCollector, null, null, compilationUnits);

            // 调用编译
            boolean success = task.call();

            // 处理编译结果
            if (success) {
                // 编译成功后，会在当前默认工作目录下生成关于被编译类的 class 文件
                System.out.println("编译成功！");
                // 如果需要，可以使用 ClassLoader 加载生成的类
                // ...
            } else {
                System.out.println("编译失败！");
                // 输出诊断信息
                for (Diagnostic<? extends JavaFileObject> diagnostic : diagnosticCollector.getDiagnostics()) {
                    System.out.println(diagnostic.getMessage(null));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("读取了错误的源文件：" + sourcePath);
        } finally {
            // 关闭文件管理器
            try {
                fileManager.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 定义一个简单的 JavaFileObject 实现，用于表示源代码，用于从内存中获取并持有代码
    private static class JavaSourceFromString extends SimpleJavaFileObject {
        final String code;

        JavaSourceFromString(String name, String code) {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }
}
