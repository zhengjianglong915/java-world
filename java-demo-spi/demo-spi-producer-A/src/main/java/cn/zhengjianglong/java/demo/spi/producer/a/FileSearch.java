package cn.zhengjianglong.java.demo.spi.producer.a;

import cn.zhengjianglong.java.demo.spi.Search;

/**
 * @author: zhengjianglong
 * @create: 2018-05-02 10:28
 */
public class FileSearch implements Search {
    @Override
    public String search(String key) {

        return "FileSearch: search result from producer A, key=" + key;
    }
}
