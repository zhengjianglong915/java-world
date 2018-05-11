package cn.zhengjianglong.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author: zhengjianglong
 * @create: 2018-05-07 18:16
 */
public class FileChannelDemo {

    /**
     * 利用文件通道读取文件内容
     *
     * @param path
     *
     * @throws IOException
     */
    public void read(String path) throws IOException {
        // FileOutputStream 也可以
        RandomAccessFile file = new RandomAccessFile(getClass().getClassLoader().getResource(path).getPath(), "rw");

        // (1) 从 FileInputStream 获取 Channel
        FileChannel fileChannel = file.getChannel();

        // (2) 创建 Buffer
        ByteBuffer buffer = ByteBuffer.allocate(48);

        // (3) 将数据从 Channel 读到 Buffer 中
        int bytesRead = fileChannel.read(buffer);
        System.out.println(bytesRead);
        while (bytesRead != -1) {
            System.out.println("Read " + bytesRead);
            buffer.flip();

            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
            }

            buffer.clear();
            bytesRead = fileChannel.read(buffer);
        }

        file.close();
    }

    /**
     * 利用文件通道向文件写内容
     *
     * @param path
     * @param content
     *
     * @throws IOException
     */
    public void write(String path, byte[] content) throws IOException {
        // 1. 获取通道
        FileOutputStream out = new FileOutputStream(path);
        FileChannel channel = out.getChannel();

        // 2. 创建buffer, 并存入数据
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        for (int i = 0; i < content.length; ++i) {
            buffer.put(content[i]);
        }
        buffer.flip();  // 关键

        // 3. 写入缓冲区
        channel.write(buffer);
        out.flush();
        out.close();
    }

    /**
     * 两个文件的copy操作
     *
     * @param inPath
     * @param outPath
     * @throws IOException
     */
    public void copy(String inPath, String outPath) throws IOException {
        FileInputStream in = new FileInputStream(inPath);
        FileOutputStream out = new FileOutputStream(outPath);

        FileChannel inChannel = in.getChannel();
        FileChannel outChannel = out.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        int readBytes = inChannel.read(buffer);
        while (readBytes != -1) {
            // 关键
            buffer.flip();
            outChannel.write(buffer);

            // 重设缓冲区
            buffer.clear();
            readBytes = inChannel.read(buffer);
        }

    }

    public static void main(String[] args) throws IOException {
        FileChannelDemo test = new FileChannelDemo();
        // test.read("nio-data.txt");

        // test.write("nio-write.txt", "hello world!".getBytes());

        test.copy("nio-data.txt", "nio-data-copy.txt");
    }
}
