package com.mine.python;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author CaoY
 * @date 2023-04-30 16:43
 * @description Java 与 python 交互的测试3
 * 下面测试如果调用的 python 脚本需要在 anaconda 虚拟环境中，且需要传递一定的参数
 */
public class PythonCaller1Test3 {

    @Test
    public void test1() {
        // conda 的虚拟环境名：
        String condaEnvironmentName = "****";
        // anaconda 的 conda 命令的文件
        String condaExecutablePath = "*******\\app\\condabin\\conda.bat"; // 根据实际情况设置 conda 可执行文件的路径
        // 要执行的脚本文件的路径
        String pythonScriptPath = "**\\container-number-identification\\pretreat.py";

        // 设置工作目录
        String workingDirectoryPath = "************\\container-number-identification";
        File workingDirectory = new File(workingDirectoryPath);

        // 创建一个命令列表，包括 conda 命令、虚拟环境名称、python 脚本路径以及脚本参数
        List<String> command = new ArrayList<>(Arrays.asList(
                condaExecutablePath,
                "run",
                "-n",
                condaEnvironmentName,
                "python",
                pythonScriptPath,
                "--source",
                "*******\\IMG_0154_0.jpg"
        ));

        // 定义执行命令：启动虚拟环境(condaEnvironmentName)，并执行脚本
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        // 合并错误输出和标准输出
        processBuilder.redirectErrorStream(true);

        // 设置 Python 脚本的工作目录
        processBuilder.directory(workingDirectory);

        try {
            // 开始执行脚本
            Process process = processBuilder.start();
            // 按一定的编码格式读取字符，这里经过测试用GBK才能确保含中文输出的日志正常，UTF-8怎么调也不行，
            // 如果有错误，欢迎指教
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(),  "GBK"));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("Python script exited with code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
