class DefaultTypeArgs() {

    class WithDefaultTypeArgs<X=Float,Y=Float>(X x, Y y) {}
    WithDefaultTypeArgs<Float,Float> wdta0 = WithDefaultTypeArgs(1.0,2.0);
    WithDefaultTypeArgs<Float> wdta1 = wdta0;
    WithDefaultTypeArgs wdta2 = wdta1;
    WithDefaultTypeArgs wdta3 = wdta0;
    WithDefaultTypeArgs<Float,Float> wdta4=wdta2;
    WithDefaultTypeArgs<Float,Float> wdta5=wdta1;
    
    class WithIllegalDefaultTypeArg<X,@error Y=X>(X x, Y y) {}
    
    class WithNestedIllegalDefaultTypeArg<X,@error Y=List<X>>(X x, Y y) {}
    
    interface I<T=String,@error S> {}
    interface J<T,@error T> {}
}