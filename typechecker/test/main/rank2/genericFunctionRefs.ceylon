[Integer,T] f<T>(Integer i, T t) => [i,t];

Val plusy<Val>(Val x, Val y) => y;

T func<T>(T t) given T satisfies String => t;

V binary<V>(V u, V v) 
        given V of Integer|Float {
    throw;
}

class Outer<X>(X x) {
    shared class Inner<Y>(Y y) {}
    shared T id<T>(T t) => t;
    shared <R> => {R*}(R(Character)) mapped = "hello".map;
}

shared void run() {
    <T> => [Integer,T](Integer,T) fun = f;
    @type:"<T> => Callable<Tuple<Integer|T,Integer,Tuple<T,T,Empty>>,Tuple<Integer|T,Integer,Tuple<T,T,Empty>>>" 
    value foobar = fun;
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
    
    <R> => {R*}(R(Character)) mapped1 = "hello".map;
    <R> => {R*}(Nothing(Anything)) mapped2 = "hello".map;
    <T> => T(T) idref = Outer("").id;
    <S> => Outer<Integer>.Inner<S>(S) innref = Outer(1).Inner;
    {String*} strings = Outer(3).mapped<String>(Object.string);
    
    value mapped = "hello world 2".map;
    function getmapped(String str) => str.map;
    @type:"Iterable<Integer,Null>" 
    value result0 = mapped(Character.integer);
    @type:"Iterable<Integer,Null>" 
    value result1 = (mapped)(Character.integer);
    @type:"Iterable<Integer,Null>"
    value result2 = mapped<Integer>(Character.integer);
    @type:"Callable<Iterable<Integer,Null>,Tuple<Callable<Integer,Tuple<Character,Character,Empty>>,Callable<Integer,Tuple<Character,Character,Empty>>,Empty>>"
    value applied = mapped<Integer>;
    @type:"<Result> => Callable<Iterable<Result,Null>,Tuple<Callable<Result,Tuple<Character,Character,Empty>>,Callable<Result,Tuple<Character,Character,Empty>>,Empty>>"
    value unapplied = mapped;
    @type:"Iterable<Boolean,Null>" 
    value result3 = getmapped("hello world 3")(Character.digit);
    @type:"Iterable<Boolean,Null>" 
    value result4 = (getmapped("hello world 3"))(Character.digit);
    
    @error print(Outer(3).mapped.hash);
    @error print(mapped.string);
    @error value val = "hello".map<Object>.string;
    
    @error <V> => V(V, V) badbinref = binary;
    <V> given V of Integer|Float => V(V, V) binref1 = binary;
    @error <V> given V of Integer|Float|String => V(V, V) binref2 = binary;
    <V> given V of Integer => V(V, V) binref3 = binary;
    @type:"<V> given V of Integer|Float => Callable<V,Tuple<V,V,Tuple<V,V,Empty>>>" 
    value infbinref = binary;
    infbinref(1,2);
    @error infbinref("hello", "world");
    @error infbinref(1,2.0);
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

void higherRank() {
    <Result> => {Result*}(Result(Element)) higher<Element>({Element*} elements) => elements.map;
    
    <T> => <<R> => {R*}(R(T))>({T*}) higherref = higher;

    <<Rs> => {Rs*}(Rs(String))>({String*}) higherdirect = higher<String>;

    <<Rs> => {Rs*}(Rs(String))>({String*}) higherapplied = higherref<String>;
    
    {Integer*} result = higherref("hello world")(Character.integer);

    void rank2(<T> => T(T) rank1Arg, Integer i) => rank1Arg(i);
    value refToRank2 = rank2;
    rank2(identity, 0);
    refToRank2(identity, 1);
}

<Element> => Singleton<Element>(Element) 
sing = Singleton;

<Element> => Singleton<Element>(Element)
anon = <Element>(Element e) => Singleton(e);


void callThem() {
    Singleton<String> s1 = sing("");
    Singleton<String> s2 = anon("");
    Singleton<String> s3 = (<E>(E e) => Singleton(e))("");
}

void refPassing() {
    @type:"Boolean" function always<T>(T t) => true;
    @type:"<T> => Callable<Boolean,Tuple<T,T,Empty>>" 
    value all = always;
    @type:"Boolean" value bool1 = "hello world".any(always);
    @type:"Boolean" value bool2 = "hello world".any(all);
    @type:"<Value> given Value satisfies Summable<Value> => Callable<Value,Tuple<Value,Value,Tuple<Value,Value,Empty>>>" 
    value pl = plus;
    Integer sum1 = [1,2,3].fold(0)(plus);
    Integer sum2 = [1,2,3].fold(0)(pl);
    @type:"<Value> => Callable<Value,Tuple<Value,Value,Empty>>" 
    value id = identity;
    @type:"Iterable<Character,Null>" 
    value chars1 = "hello world".map(identity);
    @type:"Iterable<Character,Null>" 
    value chars2 = "hello world".map(id);
}

void moreRefPassing() {
    value pl = plus;
    value sing = Singleton;
    class Holder() {
        shared <V> given V satisfies Summable<V> => V(V,V) 
        pl = plus;
        shared <E> => Singleton<E>(E) 
        sing = Singleton;
    }
    Integer sum1 = [1,2,3].fold(0)(pl);
    {Singleton<Integer|String>*} it1 = [1,2,""].map(sing);
    
    Integer sum2 = [1,2,3].fold(0)(Holder().pl);
    {Singleton<Integer|String>*} it2 = [1,2,""].map(Holder().sing);
}
