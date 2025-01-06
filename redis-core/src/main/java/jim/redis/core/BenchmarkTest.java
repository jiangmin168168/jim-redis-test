package jim.redis.core;

import cn.hutool.core.lang.Assert;
import com.google.common.base.Objects;
import jim.redis.core.service.JedisService;
import jim.redis.core.service.RedissonService;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@CompilerControl(value = CompilerControl.Mode.INLINE)
public class BenchmarkTest {
    private static final String jedisKey = "jedisKey";
    private static final int expireTime = 1000;
    private static final String redissonKey = "redissonKey";
    static {
        String redisHost="127.0.0.1";
        String redisPort="6379";
        String redisPassword="";
        JedisService.init(redisHost, redisPort, redisPassword);
        RedissonService.init(redisHost, redisPort, redisPassword);
    }

    @Setup(Level.Trial)
    public void init() {
        JedisService.set(jedisKey,"jedisValue",expireTime);
        RedissonService.set(redissonKey,"redissonValue",expireTime);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public Object benchmarkJedis() {
        String jedisValue = JedisService.get(jedisKey);
        Assert.isTrue(Objects.equal(jedisValue,"jedisValue"),"jedisValue is not equal to redisValue");
        return jedisValue;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public Object benchmarkRedisson() {
        String redissonValue = RedissonService.get(redissonKey);
        Assert.isTrue(Objects.equal(redissonValue,"redissonValue"),"redissonValue is not equal to redisValue");
        return redissonValue;
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(BenchmarkTest.class.getSimpleName())
                .forks(1)
                .warmupIterations(5)
                .warmupTime(TimeValue.seconds(2))
                .measurementIterations(10)
                .measurementTime(TimeValue.seconds(2))
                .threads(1)
                .output("/Users/jiangmin/java/demo/jim-redis-test/redis-core/target/benchmark.log")
                .build();

        new Runner(opt).run();
    }
}
