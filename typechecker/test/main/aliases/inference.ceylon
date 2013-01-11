void inference() {
    class S<T>(T t) given T satisfies Object => Singleton<T>(t);
    S<Float> s1 = S(1.0);
    S<String> s2 = S("hello");
    S<Float> s3 = S { t=1.0; };
    S<String> s4 = S { t="hello"; };
    
    class SL<T>(List<T> list) given T satisfies Object => Singleton<List<T>>(list);
    SL<Float> sl1 = SL([1.0, 2.0]);
    SL<Float> sl2 = SL { list=[1.0, 2.0]; };
    
    class SSS<T>(Singleton<Singleton<T>> sing) given T satisfies Object => Singleton<Singleton<Singleton<T>>>(sing);
    SSS<Float> sss1 = SSS(Singleton(Singleton(1.0)));
    SSS<String> sss2 = SSS { sing=Singleton(Singleton("hello")); };
    
    class E<U,V>(V v, U u) given U satisfies Object given V satisfies Object => Entry<V,U>(v,u);
    E<String,Integer> e = E(1, "hello");
    
    @error class ESL<T>(SSS<T> sss, SL<T> sl) given T satisfies Object => E<SL<T>, SSS<T>>(sss,sl);
    @type:"ESL<String>" value esl1 = ESL(SSS(Singleton(Singleton("hello"))), SL(["goodbye"]));
    ESL<Integer> esl2 = ESL(SSS(Singleton(Singleton(1))), SL([2,3]));
    Entry<Singleton<Singleton<Singleton<String>>>,Singleton<List<String>>> esl3 = 
            ESL(SSS(Singleton(Singleton("hello"))), SL(["goodbye"]));
}
