package jim.redis.core.codec;

import org.apache.fury.Fury;
import org.apache.fury.ThreadSafeFury;
import org.apache.fury.config.Language;

public class FurySerializeUtil {
    private static final ThreadSafeFury fury = Fury.builder().withLanguage(Language.JAVA)
            .requireClassRegistration(true)
            .withStringCompressed(true)
            .withAsyncCompilation(true)
            .buildThreadSafeFury();

    public static void registerClass(Class clazz) {
        fury.register(clazz);
    }

    public static <T> byte[] serialize(T value) {
        return fury.serialize(value);
    }

    public static <T> T deserialize(byte[] bytes) {
        return (T)fury.deserialize(bytes);
    }
}
