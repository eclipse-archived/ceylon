[Integer,T] f<T>(Integer i, T t) => [i,t];

shared void run() {
    <T> => [Integer,T](Integer,T) fun = f;
    <T> => [Object,T](Nothing,T) funk = f;
    @error <T> => [Float,T](Integer,T) fun0 = f;
    @error <T> => [Integer,T](Float,T) fun1 = f;
    <X> => [Object,X](Integer,X) fun2 = fun;
    @error <X> => [Float,X](Integer,X) fun3 = fun;
    @error <X> => [Integer,X](String,X) fun4 = fun;
    <E> => Singleton<E>(E) createSingleton = Singleton;
    <V> => V(V,V) p = plus;
    Float(Float,Float) addFloats = p<Float>;
    Float x = p<Float>(1.0, 2.0);
    <T> => T(T,T) getPlus() => plus;
    <V> => V(V,V) q = getPlus();
}