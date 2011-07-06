class Union() {
    interface Hello {
        shared formal String hello;
    }
    class X() satisfies Hello {
        shared actual String hello = "hello";
    }
    class Y() satisfies Hello {
        shared actual String hello = "hola";
    }
    
    X|Y xy = X();
    
    String xys = xy.hello;
    Hello h = xy;

    interface Container<out T> {
        shared formal String hello;
    }
    class U() satisfies Container<String> {
        shared actual String hello = "hello";
    }
    class V() satisfies Container<Bottom> {
        shared actual String hello = "hola";
    }
    
    U|V uv = U();
    
    String uvs = uv.hello;
    Container<String> c = uv;
    
    Container<Bottom> cb = V();
    Container<String> ss  = cb;

    interface BadContainer<T> {
        shared formal String hello;
    }
    class BadU() satisfies BadContainer<String> {
        shared actual String hello = "hello";
    }
    class BadV() satisfies BadContainer<Bottom> {
        shared actual String hello = "hola";
    }
    
    BadU|BadV buv = BadU();
    
    @error String buvs = buv.hello;
    @error Container<String> bc = buv;
    
    BadContainer<Bottom> bcb = BadV();
    @error BadContainer<String> bss  = bcb;

}