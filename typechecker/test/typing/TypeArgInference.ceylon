class TypeArgInference() {
    
    class X<T>(T t, T s) {}
    @type["TypeArgInference.X<String>"] X("hello", "world");
    @type["TypeArgInference.X<Float|Natural>"] X(1.0, 1000);

    //U first<U>(U u, U v) { return u; }
    //@type["String"] first("hello", "world");
    
    @error X x = X(1,2);
    
    //@error String firstString(String x, String y) = first;
    //String secondString(String x, String y) = first<String>;
        
    class Const<T,S>(T t) given T satisfies Numeric<T> {}
    @type["TypeArgInference.Const<Natural,Bottom>"] Const(1);
    @error Const("hello");

}