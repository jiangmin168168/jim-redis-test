package jim.redis.core;

import cn.hutool.core.lang.Assert;
import com.google.common.base.Objects;
import com.google.common.base.Stopwatch;
import jim.redis.core.service.JedisService;
import jim.redis.core.service.RedissonService;
import java.util.concurrent.TimeUnit;

public class RunTest {

    private static final String jedisKey = "jedisKey";
    private static final int expireTime = 1000;
    private static final String redissonKey = "redissonKey";

    public static void main(String[] args) {

        String redisHost="127.0.0.1";
        String redisPort="6379";
        String redisPassword="";
        JedisService.init(redisHost, redisPort, redisPassword);
        RedissonService.init(redisHost, redisPort, redisPassword);
        JedisService.set(jedisKey,"jedisValue",expireTime);
        RedissonService.set(redissonKey,"redissonValue",expireTime);
        int testCount=10000;
        //warmup
        int warmupTestCount=100000;

        for(int i=0;i<warmupTestCount;i++){
            String jedisValue = JedisService.get(jedisKey);
            Assert.isTrue(Objects.equal(jedisValue,"jedisValue"),"jedisValue is not equal to redisValue");
            String redissonValue = RedissonService.get(redissonKey);
            Assert.isTrue(Objects.equal(redissonValue,"redissonValue"),"redissonValue is not equal to redisValue");
        }
        Stopwatch stopwatch= Stopwatch.createUnstarted();

        stopwatch.start();
        for(int i=0;i<testCount;i++) {
            String jedisValue = JedisService.get(jedisKey);
            Assert.isTrue(Objects.equal(jedisValue,"jedisValue"),"jedisValue is not equal to redisValue");
        }
        stopwatch.stop();
        long jedisAvgTime=stopwatch.elapsed(TimeUnit.MICROSECONDS)/testCount;

        stopwatch.start();
        for(int i=0;i<testCount;i++) {
            String redissonValue = RedissonService.get(redissonKey);
            Assert.isTrue(Objects.equal(redissonValue,"redissonValue"),"redissonValue is not equal to redisValue");
        }
        stopwatch.stop();
        long redissonAvgTime=stopwatch.elapsed(TimeUnit.MICROSECONDS)/testCount;

        String result=
                " jedisAvgTime="+jedisAvgTime
                +" redissonAvgTime="+redissonAvgTime;
        System.out.println(result);
    }
}
