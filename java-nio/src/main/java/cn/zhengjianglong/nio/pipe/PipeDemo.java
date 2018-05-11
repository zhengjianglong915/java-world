package cn.zhengjianglong.nio.pipe;

import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author: zhengjianglong
 * @create: 2018-05-11 19:16
 */
public class PipeDemo {

    ExecutorService exec = Executors.newFixedThreadPool(2);
    // 1. 创建pipe
    Pipe pipe;

    public PipeDemo() throws Exception {
        pipe = Pipe.open();
    }

    /**
     * 向管道写数据，将数据传给另一个线程
     */
    private void sink() {
        exec.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Pipe.SinkChannel sinkChannel = pipe.sink(); // 向通道中写数据
                while (true) {
                    TimeUnit.SECONDS.sleep(1);
                    String newData = "Pipe Test At Time " + System.currentTimeMillis();
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    buf.clear();
                    buf.put(newData.getBytes());
                    buf.flip();

                    while (buf.hasRemaining()) {
                        System.out.println(buf);
                        sinkChannel.write(buf);
                    }
                }
            }
        });
    }

    /**
     * 从管道读取数据
     */
    private void source() {
        exec.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Pipe.SourceChannel sourceChannel = pipe.source(); // 向通道中读数据
                while (true) {
                    TimeUnit.SECONDS.sleep(1);
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    buf.clear();
                    int bytesRead = sourceChannel.read(buf);
                    System.out.println("bytesRead=" + bytesRead);
                    while (bytesRead > 0) {
                        buf.flip();
                        byte b[] = new byte[bytesRead];
                        int i = 0;
                        while (buf.hasRemaining()) {
                            b[i] = buf.get();
                            System.out.printf("%X", b[i]);
                            i++;
                        }
                        String s = new String(b);
                        System.out.println("=================||" + s);
                        bytesRead = sourceChannel.read(buf);
                    }
                }
            }
        });
    }

    public static void main(String[] args) throws Exception {
        PipeDemo demo = new PipeDemo();
        demo.sink();
        demo.source();
    }
}
