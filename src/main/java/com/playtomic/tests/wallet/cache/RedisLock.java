package com.playtomic.tests.wallet.cache;

import lombok.NoArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
import java.util.Date;

/**
 * Created by Orkun Cavdar on 08/02/2022
 */
@Component
@NoArgsConstructor
public class RedisLock {

    private static final Log log = LogFactory.getLog(RedisLock.class);
    private static final String KEY_PREFIX = "REDIS_LOCK_";
    private Jedis jedis;
    private int expireTime;
    private int timeout;

    public RedisLock(Jedis jedis, int timeout, int expireTime) {
        this.jedis = jedis;
        this.timeout = timeout;
        this.expireTime = expireTime;
    }

    public String setNX(final String key, final String value) {
        return jedis.set(key, value, SetParams.setParams().nx().px(expireTime));
    }

    public String setNX(final String key, final String value, final int expireTime) {
        return jedis.set(key, value, SetParams.setParams().nx().px(expireTime));
    }

    public String lock(String key, String value, int expireTime) {
        try {
            key = KEY_PREFIX + key;
            if ("OK".equals(setNX(key, value, expireTime))) {
                log.info("Redis key is Locked. Key : " + key + " ,Value : " + value);
                return value;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public String tryLock(String key, String value) {
        try {
            key = KEY_PREFIX + key;
            Long firstTryTime = new Date().getTime();
            do {
                if ("OK".equals(setNX(key, value))) {
                    log.info("Redis key is Locked. Key : " + key + ", Value : " + value);
                    return value;
                }
                log.info("Redis Lock Failure, Waiting for next try in 500 ms");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while ((new Date().getTime() - this.timeout) < firstTryTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean unLock(String key, String value) {
        Long RELEASE_SUCCESS = 1L;
        try {
            key = KEY_PREFIX + key;
            String command = "if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
            if (RELEASE_SUCCESS.equals(jedis.eval(command, Collections.singletonList(key), Collections.singletonList(value)))) {
                log.info("Redis key lock is released. Key : " + key + ", Value : " + value);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
