package jim.redis.core.codec;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class KryoSerializeUtil {
	private static Logger logger = LoggerFactory.getLogger(KryoSerializeUtil.class);

    public static <T> byte[] serialize(T value) {
        Kryo kryo = KryoUtil.borrowKryo();
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            Output output = new Output(bos);
            kryo.writeClassAndObject(output, value);
            output.close();
            return bos.toByteArray();
        } catch (Exception e) {
            logger.error("kryo# [serialize] error in value common serialization, class:" + (value == null ? "null" : value.getClass()) + ",exception:" + e.getMessage());
            return new byte[0];
        } finally {
            KryoUtil.releaseKryo(kryo);
        }
    }

    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (bytes == null) return null;
        Kryo kryo = KryoUtil.borrowKryo();
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            Input input = new Input(byteArrayInputStream, bytes.length);
            T result = (T) kryo.readClassAndObject(input);
            if (result == null) {
            	return null;
            }
            if (!clazz.isAssignableFrom(result.getClass())) {
                result = null;
            }
            input.close();
            return result;
        } catch (Exception e) {
            logger.error("kryo# [deserialize] error in common deserialization, deserialize class:" + clazz.getName()
                    + ", classType: " + clazz.getComponentType() + ", exception:" + e.getMessage(), e);
            return null;
        } finally {
            KryoUtil.releaseKryo(kryo);
        }
    }
}
