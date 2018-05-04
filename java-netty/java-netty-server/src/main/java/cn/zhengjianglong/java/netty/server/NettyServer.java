package cn.zhengjianglong.java.netty.server;

import cn.zhengjianglong.java.netty.common.NettyChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author: zhengjianglong
 * @create: 2018-05-04 16:15
 */
public class NettyServer {
    private int bindPort;

    public NettyServer(int bindPort) {
        this.bindPort = bindPort;
    }

    public void bind() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new NettyChannelInitializer(new ServerChannelHandler()))  //(4)
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            System.out.println("启动服务, 端口:" + bindPort);
            // 绑定端口，开始接收进来的连接
            ChannelFuture f = b.bind(bindPort).sync(); // (7)
            // 等待服务器  socket 关闭 。
            // 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            System.out.println("SimpleChatServer 关闭了");
        }

    }

    public static void main(String[] args) {
        NettyServer server = new NettyServer(8500);
        server.bind();

    }
}
