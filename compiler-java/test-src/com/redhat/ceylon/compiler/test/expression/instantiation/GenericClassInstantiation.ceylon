@nomodel
class Single<Element>(Element e) {
}

@nomodel
shared class KlassTypeParams<U,V>(U u, V v) {
    shared U foo(U u, V v){
        return u;
    }
}

@nomodel
class KlassTypeParamsInstantiation(){
    shared KlassTypeParams<String, Natural> m() {
        return KlassTypeParams<String, Natural>("foo", 2);
    }
    shared String m2() {
        value k = KlassTypeParams<String, Natural>("foo", 2);
        return k.foo("hello", 1);
    }
    shared String m3() {
        value k = KlassTypeParams<String, Natural>("foo", 2);
        return k.foo{u = "hello"; v = 1;};
    }
    shared void typeArgumentInference(){
    	value s = Single(69);
    }
}