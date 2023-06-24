package com.mine.java.io;

import com.mine.java.stream.entity.User;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author CaoY
 * @date 2023-06-23 17:17
 * @description IO流学习 - Files 相关操作
 * 下面多个例子演示了 Files 在不同方面的基本操作，其主要配合 Path 一起工作，实现了对于（目录中）文件的
 * 拷贝、删除、遍历等操作，以及当这些场景涉及递归文件夹时的处理。
 */
public class IOTest16 {

    @Test
    public void test1() throws IOException {
        Path path = Paths.get("src", "main", "resources", "files", "words.txt");
        byte[] bytes = Files.readAllBytes(path);
        System.out.println("bytes: " + Arrays.toString(bytes));
        String str = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("字符串的内容：");
        // 根据回车符分割字符串，并且分割后的结果不包括回车符
        String[] words = str.split("\r\n|\r(?!\n)");
        System.out.println("单词：" + Arrays.toString(words));
    }

    // 下面直接使用 Files 调用读取和写入方法适用用中等长度及以下的文本
    @Test
    public void test2() throws IOException {
        Path path = Paths.get("src", "main", "resources", "files", "words.txt");
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        System.out.println("所有行的内容：" + lines);

        Path path2 = Paths.get("src", "main", "resources", "files", "files.txt");
        List<User> userList = new ArrayList<>();
        userList.add(new User(1001, "张三", "男", 23));
        userList.add(new User(1002, "李四", "女", 21));
        userList.add(new User(1003, "王五", "女", 26));
        userList.add(new User(1004, "赵六", "男", 31));
        userList.add(new User(1005, "候七", "男", 19));
        Set<String> userStrSet = userList.stream()
                .map(User::toString)
                .collect(Collectors.toSet());
        Files.write(path2, userStrSet, StandardCharsets.UTF_8);// 成功写入字符串的集合
        System.out.println("=================写入完成===================");

        List<String> list2 = new ArrayList<>();
        list2.add("============追加的内容============");
        Files.write(path2, list2, StandardOpenOption.APPEND);
        System.out.println("=================追加完成===================");
    }

    // 如果是涉及到比较大量的文本，还是需要使用前面的输入输出流
    @Test
    public void test3() throws IOException {
        Path path = Paths.get("src", "main", "resources", "files", "files.txt");
        BufferedReader br = Files.newBufferedReader(path);

        List<String> list = br.lines().filter(s -> s.contains("User")).collect(Collectors.toList());
        System.out.println("list的内容：");
        list.forEach(System.out::println);
        System.out.println("=========================================");

        br.close();
    }

    @Test
    public void test4() throws IOException, InterruptedException {
        // 至少要递归创建两层目录，createDirectories(path) 可以递归创建目录，而
        // createDirectory(path) 只能创建一层目录，其父目录必须已经存在，这里不对
        // 创建单层目录的方法做练习
        Path rightDir = Paths.get("src", "main", "resources", "files", "dir", "son1");
        Path directories = Files.createDirectories(rightDir);
        System.out.println(directories);

        // 创建第二个目录，下面的目录路径不合常理，有后缀名，但是也能成功创建
        Path wrongDir = Paths.get("src", "main", "resources", "files", "dir", "son2.txt");
        Path directories2 = Files.createDirectories(wrongDir);
        System.out.println(directories2);

        // 创建不存在的文件
        long timeMillis = System.currentTimeMillis();
        String fileName = "文件-" + String.valueOf(timeMillis) + ".txt";
        Path filePath1 = Paths.get("src", "main", "resources", "files", "dir", fileName);
        Path file1 = Files.createFile(filePath1);
        System.out.println(file1);

        // 创建已经存在的文件
        Path file2 = null;
        try {
            file2 = Files.createFile(filePath1);
        } catch (IOException e) {
            System.out.println("创建失败：文件已存在");
        }
        System.out.println("file2: " + file2);

        // 删除文件
        Files.delete(file1);

        // 删除不存在的文件：
        Path deletePath = Paths.get("src", "main", "resources", "files", "dir", "delete.txt");
        try {
            Files.delete(deletePath);
        } catch (NoSuchFileException e) {
            System.out.println("删除失败：要删除的文件不存在！");
        }

        boolean deleteRes = Files.deleteIfExists(deletePath);
        System.out.println("是否成功删除：" + (deleteRes ? "是" : "否"));

        Path tmpPath = Paths.get("src", "main", "resources", "files", "dir");
        Path tempFile1 = null;
        try {
            // 注意：这里的 tmpPath 提供的是已经存在的文件夹：否则会报 NoSuchFileException 异常
            tempFile1 = Files.createTempFile(tmpPath, "pre", ".txt");
        } catch (NoSuchFileException e) {
            System.out.println("创建临时文件夹失败：没有这样的文件");
        }
        System.out.println("临时文件1：" + tempFile1);
        Files.deleteIfExists(tempFile1);

        Path tmpDir = Files.createTempDirectory(tmpPath, "tmp_");
        System.out.println("临时文件夹：" + tmpDir);
        Files.deleteIfExists(tmpDir);
    }

    // 复制和移动操作，如果目标文件已存在，则会报错
    @Test
    public void test5() throws IOException {
        Path srcPath = Paths.get("src", "main", "resources", "files", "words.txt");
        Path toPath = Paths.get("src", "main", "resources", "files", "copy_words.txt");
        Path copyPath = null;
        try {
            copyPath = Files.copy(srcPath, toPath);
        } catch (IOException e) {
            System.out.println("复制文件失败：目标文件已存在");
        }
        System.out.println("复制文件的路径：" + copyPath);

        Path moveToPath = Paths.get("src", "main", "resources", "files", "dir", "move.txt");
        Path moveFile = null;
        try {
            // 设置移动操作为原子性操作，且覆盖已有的目标路径
            moveFile = Files.move(toPath, moveToPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            System.out.println("移动失败：目标文件已存在");
        }
        System.out.println("移动后的文件：" + moveFile);

        // 此外也可以使用流操作进行移动和拷贝，这里不做练习
    }

    // 获取文件信息
    @Test
    public void test6() throws IOException {
        Path path = Paths.get("src", "main", "resources", "files", "words.txt");
        System.out.println("文件是否存在：" + (Files.exists(path) ? "是" : "否"));
        System.out.println("文件是否隐藏：" + (Files.isHidden(path) ? "是" : "否"));
        System.out.println("文件是否可读：" + (Files.isReadable(path) ? "是" : "否"));
        System.out.println("文件是否可写：" + (Files.isWritable(path) ? "是" : "否"));
        System.out.println("文件是否可执行：" + (Files.isExecutable(path) ? "是" : "否"));
        System.out.println("文件是否为常规文件：" + (Files.isRegularFile(path) ? "是" : "否"));
        System.out.println("文件是否为目录：" + (Files.isDirectory(path) ? "是" : "否"));
        System.out.println("文件是否为符号链接：" + (Files.isSymbolicLink(path) ? "是" : "否"));
        System.out.println("文件的字节数：" + Files.size(path) + "字节");
        System.out.println("文件拥有者：" + Files.getOwner(path));

        BasicFileAttributes bfa = Files.readAttributes(path, BasicFileAttributes.class);
        System.out.println("==================文件基本属性==================");
        System.out.println("文件字节数：" + bfa.size() + "字节");
        System.out.println("文件创建时间：" + bfa.creationTime());
        System.out.println("文件最晚修改时间：" + bfa.lastModifiedTime());
        System.out.println("文件最后一次访问时间：" + bfa.lastAccessTime());
        System.out.println("文件的主键：" + bfa.fileKey());
        System.out.println("=========================================");
    }

    @Test
    public void test7() throws IOException {
        Path dirPath = Paths.get("src", "main", "resources", "files");
        // list() 会得到相应文件夹下的 stream 流，但是其不会得到子文件下的文件
        List<Path> fileList = Files.list(dirPath).collect(Collectors.toList());
        fileList.forEach(System.out::println);
        System.out.println("===========================================");
        // walk() 可以递归的处理子文件夹的文件
        List<Path> fileList2 = Files.walk(dirPath, 2).collect(Collectors.toList());
        fileList2.forEach(System.out::println);
    }

    // 使用 Files.walk() 拷贝文件夹，但是无法来删除文件夹，因为必须在删除父目录之前删除子目录，这无法通过
    // stream 流实现
    @Test
    public void test8() throws IOException {
        Path source = Paths.get("src", "main", "resources", "files");
        Path target = Paths.get("src", "main", "resources", "files_2");
        Files.walk(source).forEach(p -> {
            try {
                // 先找出每个文件或文件夹对于 source 的对路径，再将这个相对路径连接到目标文件夹后，
                // 得出最终文件或文件夹最终路径
                Path q = target.resolve(source.relativize(p));
                if (Files.isDirectory(q)) {
                    Files.createDirectories(q);
                } else {
                    Files.copy(p, q);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 下面演示了 DirectoryStream 的基本用法。DirectoryStream 是一种接口，用于以一种迭代的方式遍历目录中的
     * 条目，它以流的方式逐个访问目录中的条目，从而实现更高效的文件遍历和处理。
     * 但是需要注意的是，DirectoryStream 是一个可关闭的资源，因此最好使用 try-with-resources 结构确保它关闭。
     *
     * @throws IOException
     */
    @Test
    public void test9() throws IOException {
        Path dirPath = Paths.get("src", "main", "resources", "files");
        System.out.println("文件夹【" + dirPath + "】包含文件：");
        try (DirectoryStream<Path> entries = Files.newDirectoryStream(dirPath)) {
            for (Path entry : entries) {
                System.out.println(entry.getFileName());
            }
        }
        System.out.println("===================过滤后===================");
        // 从 files 目录（不包括子目录）中找出所有 txt 文件
//        Files.newDirectoryStream(dirPath, "*.txt").forEach(System.out::println);
        // 按书上说是可以递归匹配子文件夹里的文件的，但经我测试并没有效果。
//        Files.newDirectoryStream(dirPath, "**.txt").forEach(System.out::println);
        // ? - 匹配单个字符
//        Files.newDirectoryStream(dirPath, "????.txt").forEach(System.out::println);
        // 匹配字符集合
//        Files.newDirectoryStream(dirPath, "bin[0-9a-z].txt").forEach(System.out::println);
        // 匹配多个可选项
        Files.newDirectoryStream(dirPath, "bin{,1,2,3,4}.txt").forEach(System.out::println);
        // 转义字符，用\来转义，不做练习
    }

    /**
     * FileVisitor 是一个接口，其中 SimpleFileVisitor 是它的一个简单实现，但是我们在使用的时候，可以
     * 通过重写 SimpleFileVisitor 中的几个方法来实现我们想要的在进入目录和离开访问时的功能，具体方法如下：
     * 1. preVisitDirectory 访问目录前的方法，可以定义一些访问目录前执行的操作
     * 2. visitFile 访问目录中文件的方法，可以定义一些对于目录中文件的操作
     * 3. visitFileFailed 访问文件失败的方法，可以定义当对文件操作异常时执行的操作
     * 4. postVisitDirectory 访问目录结束后的方法，可以定义访问完目录后执行的操作
     * 然后，上述四个方法的返回类型均为 FileVisitResult，其主要有以下四个值：
     * 1. CONTINUE：继续访问
     * 2. TERMINATE：终止访问
     * 3. SKIP_SUBTREE（忽略子树）：继续访问，但是不再访问这个目录下的文件了，它下面会访问兄弟目录
     * 4. SKIP_SIBLINGS：继续访问（忽略兄弟），但是不再访问这个目录的兄弟文件（和目录）了，它将直接返回到父目录的兄弟目录
     * @throws IOException
     */
    @Test
    public void test10() throws IOException {
        Path dirPath = Paths.get("src", "main", "resources", "files");
        // 使用 walkFileTree 打印给定目录下的所有子目录，只访问目录，不访问其中的文件
        Files.walkFileTree(dirPath, new SimpleFileVisitor<Path>() {

            // 访问前
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println(dir);
                return FileVisitResult.CONTINUE;// 继续访问
            }

            // 访问文件
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                return super.visitFile(file, attrs);
            }

            // 访问失败
            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.SKIP_SUBTREE;// 继续访问，但是不再访问这个目录下的任何项了
            }

            // 访问成功后
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
        System.out.println("====================================");
    }

    /**
     * 场景：删除目录树时，需要在移除当前所有文件的目录之后，才能移除该目录
     * 概括：FileVisitor 适用于在进入或离开一个目录时需要执行一些操作的场景
     */
    @Test
    public void test11() throws IOException {
        // 删除之前确保存在下面文件夹
        Path dirPath = Paths.get("src", "main", "resources", "files_2");
        Files.walkFileTree(dirPath, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc != null) {
                    throw exc;
                }
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * zip 文件系统，下面演示了使用 zip 文件系统对压缩文档里的文件进行的操作，还是比较方便的
     */
    @Test
    public void test12() throws IOException {
        Path zipPath = Paths.get("src", "main", "resources", "files", "test.zip");
        FileSystem fileSystem = FileSystems.newFileSystem(zipPath, null);

        // 遍历zip文档中的所有文件
        Files.walkFileTree(fileSystem.getPath("/"), new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file);
                return FileVisitResult.CONTINUE;
            }
        });
        System.out.println("=================================");
        Path source = fileSystem.getPath("/bin.txt");
        Path target = Paths.get("src", "main", "resources", "files", "bin3.txt");
        Files.copy(source, target);
        System.out.println("复制压缩文档里的其中一个文件成功！");
    }
}
