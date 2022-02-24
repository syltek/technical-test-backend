package com.playtomic.tests.wallet.cache;


import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Objects;

/**
 * Created by Orkun Cavdar on 08/02/2022
 */
@Component
@NoArgsConstructor
public class RedisClient {

    private Jedis jedis;
    private RedisLock redisLock;

    //10 seconds
    private static final int LOCK_TIMEOUT = 3*1000;
    private static final int TIMEOUT = 5*1000;

    @SneakyThrows
    public void init(int port){
        JedisPool pool = new JedisPool("localhost", port);
        jedis = pool.getResource();
        redisLock = new RedisLock(jedis, TIMEOUT, LOCK_TIMEOUT);
    }

    @SneakyThrows
    public Jedis getJedis(){
        if(Objects.nonNull(jedis)){
            return jedis;
        }
        else{
            throw new Exception("Jedis could not be null");
        }
    }

    @SneakyThrows
    public RedisLock getRedisLock(){
        if(Objects.nonNull(redisLock)){
            return redisLock;
        }
        else{
            throw new Exception("RedisLock could not be null");
        }
    }
}
