package cn.zhengjianglong.java.netty.client;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import cn.zhengjianglong.java.netty.common.NettyChannelInitializer;
import cn.zhengjianglong.java.netty.common.request.Request;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author: zhengjianglong
 * @create: 2018-05-04 16:15
 */
public class NettyClient {

    private Channel channel;
    private Bootstrap bootstrap;
    private String host;
    private int port;
    // 请求队列
    private BlockingQueue<Request> requestQueue = new LinkedBlockingQueue();

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new NettyChannelInitializer(new ClientChannelHandler()));
            channel = bootstrap.connect(host, port).sync().channel();
            while (true) {
                try {
                    Request request = requestQueue.take();
                    channel.writeAndFlush(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            group.shutdownGracefully();
        }
    }

    public void send(Request msg) {
        try {
            requestQueue.put(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // channel.writeAndFlush(msg);
    }

    public static void main(String[] args) throws Exception {
        final NettyClient client = new NettyClient("localhost", 8500);
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.connect();
            }
        }).start();

        for (int i = 0; i < 100; i++) {
            Request request = new Request();
            request.setId(i + 1);
            request.setData("hello world - " + i);
            client.send(request);

            Thread.sleep(1000);
        }

    }
}
