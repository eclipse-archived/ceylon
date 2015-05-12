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
        shared Y<Z> nuevo<W,Z=T|W>() => nothing;
        shared class New<W,Z=T|W>() {}
    }
    
    X<String>.Y xy1 = X("hello").Y(["goodbye"]);
    X<String>.Y<[Integer]> xy2 = X("hello").Y([1]);
    @type:"DefaultTypeArgs.X<Integer>.Y<Integer|Float>" X(1).nuevo<Float>();
    @type:"DefaultTypeArgs.X<Integer>.New<Float,Integer|Float>" X(1).New<Float>();
    X<Integer>.New<Float> xn1 = nothing;
    X<Integer>.New<Float,Integer|Float> xn2 = xn1;
    X<Integer>.New<Float> xn3 = xn2;
    
    class G1<X,@error N=G1<Y>>() {}
    class G2<@error N=G2<>>() {}

    class Wraps<T=Anything>(T t) {}
    
    @error String<> stringWith0Args;
    @type:"Category<Object>" Category<> categoryWith0Args;
    @type:"DefaultTypeArgs.Wraps<Anything>" Wraps<> wrapsWith0Args;
    
    class Singleton<T>(T t) {}
    
    @error String<>("");
    @error Singleton<>(1);
    @type:"DefaultTypeArgs.Wraps<Anything>" Wraps<>(1.0);
    
    @type:"List<Anything>" List<> list1 = [1,2];
    @type:"List<Anything>" List list2 = ["",""];
    @type:"Comparable<Nothing>" Comparable<> comp1 = 1;
    @type:"Comparable<Nothing>" Comparable comp2 = 1;
    
}