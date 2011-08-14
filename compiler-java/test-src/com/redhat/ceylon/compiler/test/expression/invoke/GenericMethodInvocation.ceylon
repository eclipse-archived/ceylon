@nomodel
shared class KlassMethodTypeParams() {
        shared U bar<U, V>(U u, V v) {
                return u;
        }
}

@nomodel
class GenericMethodInvocation(){
    shared String m() {
        value k = KlassMethodTypeParams();
        return k.bar<String, Natural>("hello", 1);
    }
    shared String m2() {
        value k = KlassMethodTypeParams();
        return k.bar<String, Natural>{ u = "hello"; v = 1; };
    }
}