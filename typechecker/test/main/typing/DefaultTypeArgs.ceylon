class DefaultTypeArgs() {

    class WithDefaultTypeArgs<X=Float,Y=Float>(X x, Y y) {}
    WithDefaultTypeArgs<Float,Float> wdta0 = WithDefaultTypeArgs(1.0,2.0);
    WithDefaultTypeArgs<Float> wdta1 = wdta0;
    WithDefaultTypeArgs wdta2 = wdta1;
    WithDefaultTypeArgs wdta3 = wdta0;
    WithDefaultTypeArgs<Float,Float> wdta4=wdta2;
    WithDefaultTypeArgs<Float,Float> wdta5=wdta1;
    
    class WithRecDefaultTypeArg<X,Y=X>(X x, Y y) {}
    
    class WithNestedRecDefaultTypeArg<X,Y=List<X>>(X x, Y y) {}
    
    interface I<T=String,@error S> {}
    interface J<T,@error T> {}
    interface K<@error T=T> {}
    interface L<@error T=[S],S=String> {}
    
    class X<T>(T t) {
        shared class Y<S=[T]>(S s) {}
        shared Y<Z> new<W,Z=T|W>() => nothing;
        shared class New<W,Z=T|W>() {}
    }
    
    X<String>.Y xy1 = X("hello").Y(["goodbye"]);
    X<String>.Y<[Integer]> xy2 = X("hello").Y([1]);
    @type:"X<Integer>.Y<Integer|Float>" X(1).new<Float>();
    @type:"X<Integer>.New<Float,Integer|Float>" X(1).New<Float>();
    X<Integer>.New<Float> xn1 = nothing;
    X<Integer>.New<Float,Integer|Float> xn2 = xn1;

}