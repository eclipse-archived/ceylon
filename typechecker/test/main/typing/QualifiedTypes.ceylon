class QualifiedTypes() {
    class Outer<in X>() {
        shared class Inner<out Y>() {}
    }
    class One<X>() extends Outer<X>() {}
    class Two<X>() extends Outer<X>() {}
    
    Outer<Integer>.Inner<String>&Outer<Float>.Inner<String> i0 = Outer<Integer|Float>().Inner<String>();
    @type:"QualifiedTypes.Outer<Integer|Float>.Inner<String>" value i1 = i0;

    One<Integer>.Inner<String>&Outer<Float>.Inner<String> j0 = One<Integer|Float>().Inner<String>();
    @type:"QualifiedTypes.Outer<Integer|Float>.Inner<String>" value j1 = j0;

    Outer<Integer>.Inner<String>&Two<Float>.Inner<String> k0 = Two<Integer|Float>().Inner<String>();
    @type:"QualifiedTypes.Outer<Integer|Float>.Inner<String>" value k1 = k0;
}