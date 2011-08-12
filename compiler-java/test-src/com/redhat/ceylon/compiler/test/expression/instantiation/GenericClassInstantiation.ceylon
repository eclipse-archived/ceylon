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
}

@nomodel
shared class KlassMethodTypeParams() {
	    shared U bar<U, V>(U u, V v) {
		        return u;
	    }
}

@nomodel
class KlassMethodTypeParamsInstantiation(){
    shared String m() {
        value k = KlassMethodTypeParams();
        return k.bar<String, Natural>("hello", 1);
    }
}