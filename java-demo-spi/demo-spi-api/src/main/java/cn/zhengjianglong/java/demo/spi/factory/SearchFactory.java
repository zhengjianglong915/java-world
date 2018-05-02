package cn.zhengjianglong.java.demo.spi.factory;

import java.util.Iterator;
import java.util.ServiceLoader;

import cn.zhengjianglong.java.demo.spi.Search;

/**
 * @author: zhengjianglong
 * @create: 2018-05-02 10:22
 */
public class SearchFactory {
    private SearchFactory() {
    }

    public static Search newSearch() {
        Search search = null;
        /**
         * 读取所有的配置，并完成初始化
         */
        ServiceLoader<Search> serviceLoader = ServiceLoader.load(Search.class);
        Iterator<Search> searchs = serviceLoader.iterator();

        if (searchs.hasNext()) {
            // 选择一个调用
            search = searchs.next();
        }
        return search;
    }
}
