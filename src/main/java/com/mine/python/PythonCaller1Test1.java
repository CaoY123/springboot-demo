package com.mine.python;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author CaoY
 * @date 2023-04-30 16:21
 * @description Java 与 python 交互的测试1
 * 下列代码执行 hello.py脚本，并将脚本的输出打印到控制台
 */
public class PythonCaller1Test1 {

    @Test
    public void test1() {
        // pythonCommandPath 使用的 python.exe 文件的路径，如果你要使用anaconda，就使用其app下的 python.exe 的路径
        // 注意一定要定位到 python.exe 上。这里我就不展示我本机的路径了，望见谅
        String pythonCommandPath = "********\\app\\python.exe";
        // 设置要调用的 python 脚本的路径
        String pythonScriptPath = "********\\container-number-identification\\hello.py";
//        String pythonScriptPath = "./python/hello.py";
        ProcessBuilder processBuilder = new ProcessBuilder(pythonCommandPath, pythonScriptPath);
        // 合并错误输出和标准输出
        processBuilder.redirectErrorStream(true); // 合并错误输出和标准输出

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("Python script exited with code: " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
