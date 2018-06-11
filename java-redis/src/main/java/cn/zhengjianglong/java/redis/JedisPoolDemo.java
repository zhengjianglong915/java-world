package cn.zhengjianglong.java.redis;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 利用redis连接池
 *
 * @author: zhengjianglong
 * @create: 2018-06-09 14:58
 */
public class JedisPoolDemo {
    // 最大连接数
    private int maxTotal = 10;
    //
    private int maxIdle = 10;

    private boolean testOnBorrow = false;
    private boolean testWhileIdle = false;

    private long minEvictableIdleTime = 1000;
    private long timeBetweenEvictionRuns = 1000;

    private int numTestsPerEvictionRun = 10;

    private JedisPool pool;

    public JedisPoolDemo() {
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(100);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestWhileIdle(testWhileIdle);
        config.setMinEvictableIdleTimeMillis(minEvictableIdleTime);
        config.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRuns);
        config.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        pool = new JedisPool(config, "localhost", 6379);
    }

    //  从池中取jedis对象
    public Jedis getJedis() {
        Jedis jedis = pool.getResource();
        return jedis;
    }

    // 释放jedis对象回到池
    public void close(Jedis jedis) {
        pool.returnResource(jedis);
    }

    public static void main(String[] args) {
        JedisPoolDemo pool = new JedisPoolDemo();
        Jedis jedis = pool.getJedis();
        //查看服务是否运行
        System.out.println("服务正在运行: " + jedis.ping());

        //存储数据到列表中
        jedis.lpush("site-list", "Runoob");
        jedis.lpush("site-list", "Google");
        jedis.lpush("site-list", "Taobao");
        // 获取存储的数据并输出
        List<String> list = jedis.lrange("site-list", 0, 2);
        for (int i = 0; i < list.size(); i++) {
            System.out.println("列表项为: " + list.get(i));
        }
    }
}
