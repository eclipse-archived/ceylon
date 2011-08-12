@nomodel
class KlassTypeParams<U,V>(U uu, V vv) {
    U foo(U u, V v){
        return u;
    }
    U attr = uu;
    variable V var := vv;
    U getter {
    	return attr;
    }
    V getset {
    	return var;
    }
    assign getset {
    	var := getset;
    }
}