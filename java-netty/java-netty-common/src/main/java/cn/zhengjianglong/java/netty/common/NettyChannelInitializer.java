package cn.zhengjianglong.java.netty.common;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author: zhengjianglong
 * @create: 2018-05-04 16:39
 */
public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

    private ChannelHandler channelHandler;

    public NettyChannelInitializer(ChannelHandler channelHandler) {
        this.channelHandler = channelHandler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        NettyCodecAdapter adapter = new NettyCodecAdapter();

        pipeline.addLast("decoder", adapter.getDecoder());
        pipeline.addLast("encoder", adapter.getEncoder());
        pipeline.addLast("handler", channelHandler);
    }
}
