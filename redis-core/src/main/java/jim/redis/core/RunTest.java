package jim.redis.core;

import cn.hutool.core.lang.Assert;
import com.google.common.base.Objects;
import com.google.common.base.Stopwatch;
import jim.redis.core.config.RedisConfig;
import jim.redis.core.service.JedisService;
import jim.redis.core.service.RedissonService;
import java.util.concurrent.TimeUnit;

public class RunTest {

    public static void main(String[] args) {

        String redisHost= RedisConfig.host;
        String redisPort=RedisConfig.port;
        String redisPassword=RedisConfig.pwd;
        JedisService.init(redisHost, redisPort, redisPassword);
        RedissonService.init(redisHost, redisPort, redisPassword);
        JedisService.set(RedisConfig.redisKey,RedisConfig.redisValue,RedisConfig.expireTime);
        int testCount=10000;
        //warmup
        int warmupTestCount=100000;

        for(int i=0;i<warmupTestCount;i++){
            String jedisValue = JedisService.get(RedisConfig.redisKey);
            Assert.isTrue(Objects.equal(jedisValue,RedisConfig.redisValue),"jedisValue is not equal to "+RedisConfig.redisValue);
            String redissonValue = RedissonService.get(RedisConfig.redisKey);
            Assert.isTrue(Objects.equal(redissonValue,RedisConfig.redisValue),"redissonValue is not equal to "+RedisConfig.redisValue);
        }
        Stopwatch stopwatch= Stopwatch.createUnstarted();

        stopwatch.start();
        for(int i=0;i<testCount;i++) {
            String jedisValue = JedisService.get(RedisConfig.redisKey);
            Assert.isTrue(Objects.equal(jedisValue,RedisConfig.redisValue),"jedisValue is not equal to "+RedisConfig.redisValue);
        }
        stopwatch.stop();
        long jedisAvgTime=stopwatch.elapsed(TimeUnit.MICROSECONDS)/testCount;

        stopwatch.start();
        for(int i=0;i<testCount;i++) {
            String redissonValue = RedissonService.get(RedisConfig.redisKey);
            Assert.isTrue(Objects.equal(redissonValue,RedisConfig.redisValue),"redissonValue is not equal to "+RedisConfig.redisValue);
        }
        stopwatch.stop();
        long redissonAvgTime=stopwatch.elapsed(TimeUnit.MICROSECONDS)/testCount;

        String result=
                " jedisAvgTime="+jedisAvgTime
                +" redissonAvgTime="+redissonAvgTime;
        System.out.println(result);
    }
}
