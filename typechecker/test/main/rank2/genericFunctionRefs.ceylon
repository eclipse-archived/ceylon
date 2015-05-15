[Integer,T] f<T>(Integer i, T t) => [i,t];

Val plusy<Val>(Val x, Val y) => y;

T func<T>(T t) given T satisfies String => t;

shared void run() {
    <T> => [Integer,T](Integer,T) fun = f;
    <T> => [Object,T](Nothing,T) funk = f;
    @error <T> => [Float,T](Integer,T) fun0 = f;
    @error <T> => [Integer,T](Float,T) fun1 = f;
    <X> => [Object,X](Integer,X) fun2 = fun;
    @error <X> => [Float,X](Integer,X) fun3 = fun;
    @error <X> => [Integer,X](String,X) fun4 = fun;
    <E> => Singleton<E>(E) createSingleton = Singleton;
    <V> => V(V,V) p = plusy;
    Float(Float,Float) addFloats = p<Float>;
    Float x = p<Float>(1.0, 2.0);
    <T> => T(T,T) getPlus() => plusy;
    <V> => V(V,V) q = getPlus();
    
    @error:"'<T> => T(T) given T satisfies String' is not assignable to '<T> => T(T)'"
    <T> => T(T) funcRef = func;
    <T> => T(T) given T satisfies String funcRefOk = func;
    
    <Y> => Y(Y,Y) given Y satisfies Numeric<Y> multiply = times;
    Float z = multiply<Float>(1.0, 2.0);

}

void apply<V,T>(T x, T y,
    <V> => V(V,V) 
            given V satisfies Numeric<V> f) 
        given T satisfies Numeric<T> {
    Integer i = f<Integer>(3, 5);
    print(i);
    T t = f<T>(x, y);
    print(t);
}

shared void awesome() {
    apply(1.0, 2.0, times);
    apply(1, 2, plus);
}
