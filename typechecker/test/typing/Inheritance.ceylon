class Inheritance() {
    
    interface I<T> {}
    class X<T>() satisfies I<T> {}
    class Y<T>() extends X<T>() {}
    class Z() extends X<String>() {}
    class W<U,V>() extends X<V>() {}
        
    X<String> ys = Y<String>();
    
    I<String> iys = ys;
    
    X<Natural> yn = Y<Natural>();
    
    I<Natural> iyn = yn;
    
    X<String> z = Z();
    
    I<String> iz = z;
    
    X<Float> w = W<String,Float>();
    
    I<Float> iw = w;
    
}