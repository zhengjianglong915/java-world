package cn.zhengjianglong.nio.channel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * 参考： https://my.oschina.net/u/3761681/blog/1648431
 *
 * @author: zhengjianglong
 * @create: 2018-05-11 18:32
 */
public class DatagramChannelDemo {

    public void client() throws Exception {
        DatagramChannel channel = DatagramChannel.open();
        String newData = "New String to write to file...";
        ByteBuffer buf = ByteBuffer.allocate(100);
        buf.clear();
        buf.put(newData.getBytes());

        buf.flip();
        channel.send(buf, new InetSocketAddress("localhost", 8200));
    }

    public void server() throws Exception {
        DatagramChannel channel = DatagramChannel.open();
        channel.socket().bind(new InetSocketAddress(8200));
        ByteBuffer buf = ByteBuffer.allocate(48);
        channel.receive(buf);

        buf.flip();
        while (buf.hasRemaining()) {
            System.out.print((char) buf.get());
        }

    }

    public static void main(String[] args) throws Exception {
        final DatagramChannelDemo demo = new DatagramChannelDemo();

        // 服务端
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    demo.server();
                } catch (Exception e) {

                }
            }
        }).start();

        // 客户端
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    demo.client();
                } catch (Exception e) {

                }
            }
        }).start();

    }
}
