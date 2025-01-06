package jim.redis.core.service;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.*;
import java.util.concurrent.locks.ReentrantLock;
@Slf4j
public class JedisService {
    private static JedisPool jedisPool;
    private static volatile boolean initialized = false;
    private static final ReentrantLock lock=new ReentrantLock();
    public static synchronized void init(String redisHost, String redisPort, String redisPassword) {
        if(initialized){
            return;
        }
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(50);
        jedisPoolConfig.setMaxIdle(50);
        jedisPoolConfig.setMinIdle(10);
        jedisPoolConfig.setMaxWaitMillis(1000);
        jedisPoolConfig.setTestWhileIdle(true);

        try {
            lock.lock();
            if (initialized) {
                lock.unlock();
                return;
            }
            jedisPool = new JedisPool(jedisPoolConfig,redisHost, Integer.parseInt(redisPort), 1000,redisPassword);
            initialized = true;
        }
        catch (Throwable ex){
            throw ex;
        }
        finally {
            lock.unlock();
        }
    }

    public static String get(String key){
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }
    }
    public static void set(String key, String value,int timeToLiveInSeconds){
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex(key, timeToLiveInSeconds, value);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
