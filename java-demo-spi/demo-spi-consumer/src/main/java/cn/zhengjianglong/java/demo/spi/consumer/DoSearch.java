package cn.zhengjianglong.java.demo.spi.consumer;

import cn.zhengjianglong.java.demo.spi.Search;
import cn.zhengjianglong.java.demo.spi.factory.SearchFactory;

/**
 * @author: zhengjianglong
 * @create: 2018-05-02 10:34
 */
public class DoSearch {
    public static void main(String[] args) {
        Search search = SearchFactory.newSearch();
        String result = search.search("test");
        System.out.println(result);

    }
}
