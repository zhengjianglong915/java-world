package cn.zhengjianglong.java.netty.server;

import cn.zhengjianglong.java.netty.common.request.Request;
import cn.zhengjianglong.java.netty.common.response.Response;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author: zhengjianglong
 * @create: 2018-05-04 16:40
 */
@ChannelHandler.Sharable
public class ServerChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        Request request = (Request) msg;
        System.out.println("服务端收到消息:" + request);

        Response response = new Response();
        response.setId(request.getId());
        response.setData("SERVER:" + request.getData());

        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        //……
        if (channel.isActive()) {
            ctx.close();
        }
    }

}
