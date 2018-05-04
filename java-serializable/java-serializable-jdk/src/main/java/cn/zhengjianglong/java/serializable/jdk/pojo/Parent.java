package cn.zhengjianglong.java.serializable.jdk.pojo;

import java.io.Serializable;

/**
 * @author: zhengjianglong
 * @create: 2018-05-04 14:46
 */
public class Parent implements Serializable {
    private static final long serialVersionUID = 1L;
    public int parentValue = 100;

    public class InnerObject implements Serializable {
        private static final long serialVersionUID = 2L;

        public int innerValue = 200;
    }
}
