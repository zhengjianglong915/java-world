package cn.zhengjianglong.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * https://www.cnblogs.com/qq-361807535/p/6670529.html
 *
 * @author: zhengjianglong
 * @create: 2018-05-11 19:30
 */
public class ServerSocketSelectorDemo {

    /**
     * 服务端
     *
     * @throws Exception
     */
    public void server() throws Exception {
        // 1. 获取服务端通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        ssChannel.bind(new InetSocketAddress(9898));
        // 2. 设置为非阻塞模式
        ssChannel.configureBlocking(false);

        // 3. 打开一个监听器
        Selector selector = Selector.open();
        // 4. 向监听器注册接收事件
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (selector.select() > 0) {
            // 5. 获取监听器上所有的监听事件值
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

            // 6. 如果有值
            while (it.hasNext()) {
                // 7. 取到SelectionKey
                SelectionKey key = it.next();

                // 8. 根据key值判断对应的事件
                if (key.isAcceptable()) {
                    // 9. 接入处理, 一个新的客户端接入
                    SocketChannel socketChannel = ssChannel.accept();
                    socketChannel.configureBlocking(false);
                    // 注册该客户端的读事件
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    // 10. 可读事件处理
                    SocketChannel channel = (SocketChannel) key.channel();
                    readMsg(channel);
                }
                // 11. 移除当前key
                it.remove();
            }
        }
    }

    private void readMsg(SocketChannel channel) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        int len;
        while ((len = channel.read(buf)) > 0) {
            buf.flip();
            byte[] bytes = new byte[1024];
            buf.get(bytes, 0, len);
            System.out.println(new String(bytes, 0, len));
        }
    }

    /**
     * 客户端
     *
     * @throws Exception
     */
    public void client() throws Exception {
        // 1. 获取socketChannel
        SocketChannel sChannel = SocketChannel.open();
        // 2. 创建连接
        sChannel.connect(new InetSocketAddress("127.0.0.1", 9898));
        ByteBuffer buf = ByteBuffer.allocate(1024);
        // 3. 设置通道为非阻塞
        sChannel.configureBlocking(false);

        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String msg = scanner.nextLine();

            buf.put((new Date() + "：" + msg).getBytes());
            buf.flip();
            //4. 向通道写数据
            sChannel.write(buf);
            buf.clear();
        }
    }

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(2);
        final ServerSocketSelectorDemo demo = new ServerSocketSelectorDemo();

        // 启动服务端
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    demo.server();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 启动客户端
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    demo.client();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
