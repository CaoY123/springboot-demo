package com.mine.java.io;

import org.junit.Test;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author CaoY
 * @date 2023-06-25 16:33
 * @description IO流学习 - 文件加锁机制
 *
 * 当多个进程/线程对同一文件进行操作时，有必要对文件进行加锁操作，下面练习了关于加锁的基本操作
 *
 * FileLock 的 release() 和 close()：
 * 1. release()：释放文件锁，释放后，其他进程还是可以获取并使用的；
 * 2. close()：关闭文件锁并释放与锁相关的资源，它也会自动释放锁，但是后续无法使用该文件锁来锁定文件
 *
 * 场景构建：下面要运行 test1 和 test2 测试用例来展示文件锁的效果，其中，先运行 test1，test1 负责第一次获取锁，
 * 并休眠10秒，在 test1 休眠的时候紧接着运行 test2，可以看到 test2 会等几秒再继续执行，可以说明文件锁的作用。
 */
public class IOTest18 {

    @Test
    public void test1() throws IOException, InterruptedException {
        Path path = Paths.get("src", "main", "resources", "files", "words.txt");
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.APPEND)) {
            FileLock fileLock = fileChannel.lock();
            try {
                System.out.println("Successfully acquired file lock. Press any key to release the lock...");

                Thread.sleep(10000);
                System.out.println("Do something....1");

                System.out.println("Lock released.");
                fileLock.release();
            } catch (OverlappingFileLockException e) {
                System.err.println("File already locked by another process.");
            }
        }
    }

    @Test
    public void test2() throws IOException {
        Path path = Paths.get("src", "main", "resources", "files", "words.txt");
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.APPEND)) {
            try (FileLock fileLock = fileChannel.lock()) {
                System.out.println("Successfully acquired file lock. Press any key to release the lock...");

                System.out.println("Do something....2");

                System.out.println("Lock released.");
            } catch (OverlappingFileLockException e) {
                System.err.println("File already locked by another process.");
            }
        }
    }

    // 共享锁的判断练习
    @Test
    public void test3() throws IOException {
        Path path = Paths.get("src", "main", "resources", "files", "words.txt");
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.APPEND)) {
            FileLock fileLock = fileChannel.tryLock();

            System.out.println("该锁是否支持共享？" + (fileLock.isShared() ? "是" : "否"));
        }
    }
}
