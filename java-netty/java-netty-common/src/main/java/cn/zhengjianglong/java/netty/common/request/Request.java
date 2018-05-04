package cn.zhengjianglong.java.netty.common.request;

import java.io.Serializable;

/**
 * @author: zhengjianglong
 * @create: 2018-05-04 17:21
 */
public class Request implements Serializable {
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
        return "Request{" +
                "id=" + id +
                ", data='" + data + '\'' +
                '}';
    }
}
