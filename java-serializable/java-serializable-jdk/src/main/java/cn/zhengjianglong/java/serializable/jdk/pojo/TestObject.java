package cn.zhengjianglong.java.serializable.jdk.pojo;

import java.io.Serializable;

/**
 * @author: zhengjianglong
 * @create: 2018-05-04 14:50
 */
public class TestObject extends Parent implements Serializable {
    private static final long serialVersionUID = 3L;
    public int testValue = 300;
    public InnerObject innerObject = new InnerObject();
}
