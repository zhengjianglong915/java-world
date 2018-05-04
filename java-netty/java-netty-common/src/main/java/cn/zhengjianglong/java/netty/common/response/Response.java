package cn.zhengjianglong.java.netty.common.response;

import java.io.Serializable;

/**
 * @author: zhengjianglong
 * @create: 2018-05-04 17:22
 */
public class Response implements Serializable {
    private int id;
    private String data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "id=" + id +
                ", data='" + data + '\'' +
                '}';
    }
}
