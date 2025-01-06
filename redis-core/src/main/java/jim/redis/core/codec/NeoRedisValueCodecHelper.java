package jim.redis.core.codec;

import jim.redis.core.enums.RedisCodecType;

public class NeoRedisValueCodecHelper {

    public static byte[] encodeToBytes(Object value, RedisCodecType codecType) {
        byte[]  result = new byte[0];
        if(null == value){
            return result;
        }
       if (codecType == RedisCodecType.KRYO) {
            result = KryoSerializeUtil.serialize(value);
        }
        else if (codecType == RedisCodecType.FURY) {
            result = FurySerializeUtil.serialize(value);
        }

        return result;
    }

    public static <T> T decodeFromBytes(byte[] value, RedisCodecType codecType, Class<T> clazz) {
        T result = null;
        if(null == value){
            return result;
        }

        if (codecType == RedisCodecType.KRYO) {
            result = KryoSerializeUtil.deserialize(value, clazz);
        }
        else if (codecType == RedisCodecType.FURY) {
            result = FurySerializeUtil.deserialize(value);
        }
        return result;
    }

}
