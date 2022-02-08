package com.playtomic.tests.wallet.cache;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Objects;

/**
 * Created by Orkun Cavdar on 08/02/2022
 */
@Component
public class EmbeddedRedis {

    @Value("${spring.redis.port}")
    private int redisPort;

    private RedisServer redisServer;
    private RedisClient redisClient;

    @SneakyThrows
    @PostConstruct
    public void startRedis() {
        if(!Objects.nonNull(redisServer)){
            redisServer = new RedisServer(redisPort);
            redisServer.start();

            startRedisClient();
        }
    }

    @PreDestroy
    public void stopRedis() {
        if(Objects.nonNull(redisServer)){
            redisServer.stop();
        }
    }

    private void startRedisClient(){
        redisClient = new RedisClient();
        redisClient.init(redisPort);
    }

    @SneakyThrows
    public RedisLock getRedisLock(){
        if(Objects.nonNull(redisClient) && Objects.nonNull(redisClient.getRedisLock())){
            return redisClient.getRedisLock();
        }
        else {
            throw new Exception("RedisClient or RedisLock could not be null");
        }
    }
}
