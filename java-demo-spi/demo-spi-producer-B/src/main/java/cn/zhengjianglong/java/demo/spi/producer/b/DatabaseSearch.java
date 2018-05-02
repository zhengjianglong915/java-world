package cn.zhengjianglong.java.demo.spi.producer.b;

import cn.zhengjianglong.java.demo.spi.Search;

/**
 * @author: zhengjianglong
 * @create: 2018-05-02 10:39
 */
public class DatabaseSearch implements Search {
    @Override
    public String search(String key) {

        return "DatabaseSearch: search result from producer B, key=" + key;
    }
}
