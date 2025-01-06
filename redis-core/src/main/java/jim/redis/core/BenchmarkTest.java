package jim.redis.core;

import cn.hutool.core.lang.Assert;
import com.google.common.base.Objects;
import jim.redis.core.config.RedisConfig;
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

    static {
        String redisHost= RedisConfig.host;
        String redisPort=RedisConfig.port;
        String redisPassword=RedisConfig.pwd;
        JedisService.init(redisHost, redisPort, redisPassword);
        RedissonService.init(redisHost, redisPort, redisPassword);
    }

    @Setup(Level.Trial)
    public void init() {
        JedisService.set(RedisConfig.redisKey,RedisConfig.redisValue,RedisConfig.expireTime);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public Object benchmarkJedis() {
        String jedisValue = JedisService.get(RedisConfig.redisKey);
        Assert.isTrue(Objects.equal(jedisValue,RedisConfig.redisValue),"jedisValue is not equal to "+RedisConfig.redisValue);
        return jedisValue;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public Object benchmarkRedisson() {
        String redissonValue = RedissonService.get(RedisConfig.redisKey);
        Assert.isTrue(Objects.equal(redissonValue,RedisConfig.redisValue),"redissonValue is not equal to "+RedisConfig.redisValue);
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
                .output("benchmark.log")
                .build();

        new Runner(opt).run();
    }
}
