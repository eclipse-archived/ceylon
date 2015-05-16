[Integer,T] f<T>(Integer i, T t) => [i,t];

Val plusy<Val>(Val x, Val y) => y;

T func<T>(T t) given T satisfies String => t;

class Outer<X>(X x) {
    shared class Inner<Y>(Y y) {}
    shared T id<T>(T t) => t;
    shared <R> => {R*}(R(Character)) mapped = "hello".map;
}

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
    <T> given T satisfies String => T(T) funcRefOk = func;
    
    <Y> given Y satisfies Numeric<Y> => Y(Y,Y) multiply = times;
    Float z = multiply<Float>(1.0, 2.0);

    <T> given T satisfies Numeric<T> => T(T,T) afun 
            = <T>(T x, T y) 
                given T satisfies Summable<T> 
                => x+y;
    
    <R> => {R*}(R(Character)) mapped = "hello".map;
    <R> => {R*}(Nothing(Anything)) mapped2 = "hello".map;
    <T> => T(T) idref = Outer("").id;
    <S> => Outer<Integer>.Inner<S>(S) innref = Outer(1).Inner;
    {String*} strings = Outer(3).mapped<String>(Object.string);
    
    @error print(Outer(3).mapped.hash);
    @error print(mapped.string);
    @error value val = "hello".map<Object>.string;

}

void apply<T>(T x, T y,
    <V> given V satisfies Numeric<V>
            => V(V,V) f) 
        given T satisfies Numeric<T> {
    Integer i = f<Integer>(3, 5);
    print(i);
    T t = f<T>(x, y);
    print(t);
}

shared void awesome() {
    apply(1.0, 2.0, times);
    apply(1, 2, plus);
    applyIt("hello", "world", plus);
    applyIt(1, 2, plus);
    apply(1,2, 
        <T>(T x, T y) 
            given T satisfies Summable<T> 
            => x+y);
}

alias BinaryOp<V> 
        given V satisfies Summable<V> 
        => V(V,V);

void applyIt<T>(T x, T y, BinaryOp f) 
        given T satisfies Summable<T> {
    Integer i = f<Integer>(3, 5);
    print(i);
    T t = f<T>(x, y);
    print(t);
}

