package cn.zhengjianglong.java.netty.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author: zhengjianglong
 * @create: 2018-05-04 17:00
 */
public class NettyCodecAdapter {

    private static final int HEAD_LENGTH = 4;

    private final ChannelHandler encoder = new InternalEncoder();

    private final ChannelHandler decoder = new InternalDecoder();

    /**
     * Sharable 使得多个客户端连接的时候，可以共享一个handler
     * 对对象进行编码
     */
    @ChannelHandler.Sharable
    private class InternalEncoder extends MessageToByteEncoder {
        @Override
        protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf out)
                throws Exception {

            // 对象转换为二进制
            byte[] body = toByteArray(msg);

            int dataLength = body.length;
            out.writeInt(dataLength);
            out.writeBytes(body);
        }
    }

    /**
     * 解码
     */
    private class InternalDecoder extends ByteToMessageDecoder {
        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list)
                throws Exception {

            // 这个HEAD_LENGTH是我们用于表示头长度的字节数。  由于上面我们传的是一个int类型的值，所以这里HEAD_LENGTH的值为4.
            if (in.readableBytes() < HEAD_LENGTH) {
                return;
            }

            // 我们标记一下当前的readIndex的位置
            in.markReaderIndex();

            // 读取传送过来的消息的长度。ByteBuf 的readInt()方法会让他的readIndex增加4
            int dataLength = in.readInt();

            if (dataLength < 0) { // 我们读到的消息体长度为0，这是不应该出现的情况，这里出现这情况，关闭连接。
                ctx.close();
            }

            // 读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex. 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
            if (in.readableBytes() < dataLength) {
                in.resetReaderIndex();
                return;
            }


            //  这时候，我们读到的长度，满足我们的要求了，把传送过来的数据取出来
            byte[] body = new byte[dataLength];
            in.readBytes(body);

            // 反序列化为对象
            Object object = toObject(body);
            list.add(object);
        }
    }

    public ChannelHandler getEncoder() {
        return encoder;
    }

    public ChannelHandler getDecoder() {
        return decoder;
    }

    public byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();

            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * 数组转对象
     *
     * @param bytes
     *
     * @return
     */
    public Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

}
