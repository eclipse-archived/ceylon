void assignability() {
    class In<T>(T* ts) {}
    In<out Object> i1 = In("", 1);
    In<out String> i2 = In("", "");
    In<out String|Integer> i3 = In("", 1);
    @error In<out String> i2e = i3;
    @error In<out String|Integer> i3e = i1;
    In<in Nothing> i4 = In("", "");
    In<in String> i5 = In("", "");
    In<in String|Integer> i6 = In("", 1);
    @error In<in String> i5e = i4;
    @error In<in String|Integer> i6e = i5;
    @error In<in String> i4e = i2; 
    @error In<out String> i1e = i5;
    In<out Anything> i7 = i2;
    In<out Anything> i8 = i5;
    In<in Nothing> i9 = i5;
    In<in Nothing> i10 = i2;
    In<out Nothing> i11 = In();
    In<Nothing> i12 = i11;
    In<in Anything> i13 = In<Anything>();
    In<Anything> i14 = i13;
    class Pair<U,V>(U u, V v) 
            given U satisfies Object
            given V satisfies Object {}
    interface X {}
    Pair<out String, in String> p1 = Pair("","");
    Pair<out String|X, in String&X> p2 = p1;
    Pair<out Object, in Nothing> p3 = p2;
    Pair<in Nothing, out Object> p4 = p2;
    Pair<in Nothing, out Object> p5 = p3;
    @error Pair<in String, out String> p6 = p1;
    @error Pair<String, String> p7 = p6;
    interface Co<out T> {}
    interface Contra<in T> {}
    @error Co<out Float> coout;
    @error Co<in Float> coin;
    @error Contra<out Float> contraout;
    @error Contra<in Float> contrain;
    
    Co<in Float> coinf = nothing;
    @error Co<in Object> coino = coinf;
    Co<in Nothing> coinn = coinf;
    @error Co<Object> coo = coinf; 
    Co<Anything> coa = coinf;
}