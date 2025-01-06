package jim.redis.core.enums;

import java.util.Objects;

public enum RedisCodecType {

    KRYO("kryo", "kryo体积小速度快"),
    FURY("fury", "国内高性能序列化")
    ;

    private String value;

    private String desc;

    RedisCodecType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String value() {
        return this.value;
    }

    public String desc(Object... params) {
        return String.format(this.desc, params);
    }

    public static RedisCodecType from(String value) {
        for (RedisCodecType t : RedisCodecType.values()) {
            if (Objects.equals(t.value, value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("非法的值");
    }
}
