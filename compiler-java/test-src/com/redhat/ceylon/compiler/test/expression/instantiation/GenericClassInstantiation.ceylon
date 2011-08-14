@nomodel
shared class KlassTypeParams<U,V>() {
    shared U foo(U u, V v){
        return u;
    }
}

@nomodel
class KlassTypeParamsInstantiation(){
    shared KlassTypeParams<String, Natural> m() {
        return KlassTypeParams<String, Natural>();
    }
    shared String m2() {
        value k = KlassTypeParams<String, Natural>();
        return k.foo("hello", 1);
    }
    shared String m3() {
        value k = KlassTypeParams<String, Natural>();
        return k.foo{u = "hello"; v = 1;};
    }
}