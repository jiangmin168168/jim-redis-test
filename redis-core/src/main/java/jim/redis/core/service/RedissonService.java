package jim.redis.core.service;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

import java.time.Duration;

public class RedissonService {
    private static RedissonClient redisson;
    private static volatile boolean initialized = false;
    public static synchronized void init(String redisHost, String redisPort, String redisPassword) {
        if(initialized){
            return;
        }
        Config config = new Config();
        String redisUrl = "redis://" + redisHost + ":" + redisPort;
        config.useSingleServer()
                .setAddress(redisUrl)
                .setPassword(redisPassword)
                .setConnectionPoolSize(50)
                .setConnectionMinimumIdleSize(10)
                .setConnectTimeout(1000)

        ;
        config.setCodec(new StringCodec());
        redisson = Redisson.create(config);

    }

    public static String get(String key){
        return (String)redisson.getBucket(key).get();
    }

    public static void set(String key, String value,int timeToLiveInSeconds){
        redisson.getBucket(key).set(value, Duration.ofSeconds(timeToLiveInSeconds));
    }

}
