package cn.zhengjianglong.java.serializable.jdk;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cn.zhengjianglong.java.serializable.jdk.pojo.TestObject;

/**
 * 采用java自带的序列化方式
 *
 * @author: zhengjianglong
 * @create: 2018-05-04 14:52
 */
public class SerializableTest {
    public static void main(String[] args) throws Exception {
        FileOutputStream fos = new FileOutputStream("temp.out");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        TestObject testObject = new TestObject();
        oos.writeObject(testObject);
        oos.flush();
        oos.close();

        FileInputStream fis = new FileInputStream("temp.out");
        ObjectInputStream ois = new ObjectInputStream(fis);
        TestObject deTest = (TestObject) ois.readObject();
        System.out.println(deTest.testValue);
        System.out.println(deTest.parentValue);
        System.out.println(deTest.innerObject.innerValue);
    }
}
