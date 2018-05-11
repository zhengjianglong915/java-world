package cn.zhengjianglong.nio.channel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author: zhengjianglong
 * @create: 2018-05-11 15:58
 */
public class SocketChannelDemo {

    public void client() throws Exception {

        // 1. 获取通道
        SocketChannel socketChannel = SocketChannel.open();

        // 2. 绑定端口
        socketChannel.connect(new InetSocketAddress("localhost", 9999));

        // 3. 创建buffer
        ByteBuffer buf = ByteBuffer.allocate(48);
        // 5. 发送数据
        buf.put("Hello world".getBytes());
        buf.flip();
        socketChannel.write(buf);

        // 6. 接收服务端响应
        /*buf.clear();
        int bytesRead = socketChannel.read(buf);
        while (bytesRead != - 1) {
            buf.flip();

            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }

            buf.clear();
            bytesRead = socketChannel.read(buf);
        }

        Thread.sleep(1000);*/

        // 7. 关闭
        socketChannel.close();
    }

    public static void main(String[] args) throws Exception{
        SocketChannelDemo client = new SocketChannelDemo();
        client.client();
    }
}
