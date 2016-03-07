import java.lang{
    JString=String,
    
    ObjectArray,
    
    BooleanArray,
    
    ByteArray,
    ShortArray,
    IntArray,
    LongArray,
    
    FloatArray,
    DoubleArray,
    
    CharArray
}
import ceylon.language.meta{
    typeLiteral
}
import ceylon.language.meta.model{
    Class
}

T toArray<T, X>(X[] elements) given X satisfies Object {
    assert(is Class<Anything,Nothing> t= typeLiteral<T>());
    if (`class ObjectArray` == t.declaration) {
        value r = ObjectArray<X>(elements.size);
        variable value index = 0;
        for (element in elements) {
            r.set(index++, element);
        }
        assert(is T r);
        return r;
    } else if (`BooleanArray` == t) {
        value r = BooleanArray(elements.size);
        variable value index = 0;
        for (element in elements) {
            assert(is Boolean element);
            r.set(index++, element);
        }
        assert(is T r);
        return r;
    } else if (`ByteArray` == t) {
        value r = ByteArray(elements.size);
        variable value index = 0;
        for (element in elements) {
            assert(is Byte element);
            r.set(index++, element);
        }
        assert(is T r);
        return r;
    } else if (`ShortArray` == t) {
        value r = ShortArray(elements.size);
        variable value index = 0;
        for (element in elements) {
            assert(is Integer element);
            r.set(index++, element);
        }
        assert(is T r);
        return r;
    } else if (`IntArray` == t) {
        value r = IntArray(elements.size);
        variable value index = 0;
        for (element in elements) {
            assert(is Integer element);
            r.set(index++, element);
        }
        assert(is T r);
        return r;
    } else if (`LongArray` == t) {
        value r = LongArray(elements.size);
        variable value index = 0;
        for (element in elements) {
            assert(is Integer element);
            r.set(index++, element);
        }
        assert(is T r);
        return r;
    } else if (`FloatArray` == t) {
        value r = FloatArray(elements.size);
        variable value index = 0;
        for (element in elements) {
            assert(is Float element);
            r.set(index++, element);
        }
        assert(is T r);
        return r;
    } else if (`DoubleArray` == t) {
        value r = DoubleArray(elements.size);
        variable value index = 0;
        for (element in elements) {
            assert(is Float element);
            r.set(index++, element);
        }
        assert(is T r);
        return r;
    } else if (`CharArray` == t) {
        value r = CharArray(elements.size);
        variable value index = 0;
        for (element in elements) {
            assert(is Character element);
            r.set(index++, element);
        }
        assert(is T r);
        return r;
    } else {
        "unsupported array type"
        assert(false);
    }
}
