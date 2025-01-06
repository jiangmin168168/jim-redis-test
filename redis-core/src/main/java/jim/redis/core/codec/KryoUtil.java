package jim.redis.core.codec;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.esotericsoftware.kryo.serializers.FieldSerializer;

public class KryoUtil {
    private static KryoPool kryoPool;

    public static Kryo borrowKryo() {
        if (kryoPool == null) {
            //Log.TRACE();
            kryoPool = new KryoPool.Builder(new KryoFactory() {
                @Override
                public Kryo create() {
                    Kryo kryo = new Kryo();
                    kryo.setRegistrationRequired(false);
                    kryo.setReferences(false);
                    kryo.setDefaultSerializer(FieldSerializer.class);
                    /*List<Class> classes = DaoConfig.getInstance().getClasses();
                    for (Class clazz : classes) {
                        if (clazz != null) {
                            kryo.register(clazz);
                        }
                    }*/
                    return kryo;
                }
            }).softReferences().build();
        }
        return kryoPool.borrow();
    }

    public static void releaseKryo(Kryo kryo) {
        kryoPool.release(kryo);
    }
}
