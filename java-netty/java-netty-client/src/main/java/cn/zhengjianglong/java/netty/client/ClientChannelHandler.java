package cn.zhengjianglong.java.netty.client;

import cn.zhengjianglong.java.netty.common.response.Response;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author: zhengjianglong
 * @create: 2018-05-04 17:29
 */
public class ClientChannelHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response response = (Response) msg;
        System.out.println("客户端收到消息：" + response);

    }
}
