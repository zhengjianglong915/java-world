package cn.zhengjianglong.nio.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: zhengjianglong
 * @create: 2018-05-11 15:47
 */
public class ServerSocketChannelDemo {

    public void server() throws Exception {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 10, 1000, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(100));

        System.out.println("服务端已启动....");

        // 1. 获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 2. 绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(9999));

        while (true) {
            // 3. 监听新进来的连接，一个客户端是一个channel。
            SocketChannel socketChannel = serverSocketChannel.accept();
            //每个连接使用一个单独线程处理
            if (socketChannel != null) {
                executor.submit(new SocketChannelThread(socketChannel));
            } else {
                break;
            }
        }

        serverSocketChannel.close();
    }

    /**
     * 处理现场
     */
    private class SocketChannelThread implements Runnable {

        private SocketChannel socketChannel;
        private String remoteName;

        public SocketChannelThread(SocketChannel socketChannel) throws IOException {
            this.socketChannel = socketChannel;
            this.remoteName = socketChannel.getRemoteAddress().toString();
            System.out.println("客户:" + remoteName + " 连接成功!");
        }

        @Override
        public void run() {
            // buffer，存放消息内容
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                try {
                    buffer.clear();
                    int read = socketChannel.read(buffer);
                    if (read != -1) {
                        buffer.flip();
                        while (buffer.hasRemaining()) {
                            System.out.print((char) buffer.get());
                        }
                    }
                } catch (Exception e) {
                    System.out.println(remoteName + " 断线了,连接关闭");
                    try {
                        socketChannel.close();
                    } catch (IOException ex) {
                    }
                    break;
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ServerSocketChannelDemo demo = new ServerSocketChannelDemo();
        demo.server();
    }
}
