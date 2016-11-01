import ceylon.interop.java {javaClass}
import java.lang{Class}

Class<T&Object>? sdkBug571<T>() {
    if (`T` == `Null`) {
        return null;
    } else {
        return javaClass<T&Object>();
    }
}
shared void sdkBug571_run() {
    assert(exists x1= sdkBug571<String>(),
        x1 == javaClass<String>());
    assert(exists x2 = sdkBug571<Anything>(),
        x2 == javaClass<Object>());
    assert(exists x3 = sdkBug571<Object>(),
        x3 == javaClass<Object>());
}