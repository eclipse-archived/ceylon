class QualifiedTypes() {
    class Outer<in X>() {
        shared class Inner<out Y>() {
            shared Y accept(X x, X xx) => nothing;
        }
    }
    class One<X>() extends Outer<X>() {}
    class Two<X>() extends Outer<X>() {}
    
    Outer<Integer>.Inner<String>&Outer<Float>.Inner<String> i0 = Outer<Integer|Float>().Inner<String>();
    @type:"QualifiedTypes.Outer<Integer|Float>.Inner<String>" value i1 = i0;
    Outer<Integer|Float>.Inner<String> i2 = i0;
    Outer<Integer>.Inner<String>&Outer<Float>.Inner<String> i3 = i2;
    @type:"Callable<String,Tuple<Integer|Float,Integer|Float,Tuple<Integer|Float,Integer|Float,Empty>>>" 
    value f = i0.accept;

    One<Integer>.Inner<String>&Outer<Float>.Inner<String> j0 = One<Integer|Float>().Inner<String>();
    @type:"QualifiedTypes.Outer<Integer|Float>.Inner<String>" value j1 = j0;
    Outer<Integer|Float>.Inner<String> j2 = j0;
    One<Integer>.Inner<String>&Outer<Float>.Inner<String> j3 = j2;
    @type:"Callable<String,Tuple<Integer|Float,Integer|Float,Tuple<Integer|Float,Integer|Float,Empty>>>" 
    value g = j0.accept;

    Outer<Integer>.Inner<String>&Two<Float>.Inner<String> k0 = Two<Integer|Float>().Inner<String>();
    @type:"QualifiedTypes.Outer<Integer|Float>.Inner<String>" value k1 = k0;
    Outer<Integer|Float>.Inner<String> k2 = k0;
    Outer<Integer>.Inner<String>&Two<Float>.Inner<String> k3 = k2;
    @type:"Callable<String,Tuple<Integer|Float,Integer|Float,Tuple<Integer|Float,Integer|Float,Empty>>>" 
    value h = k0.accept;
}