shared interface Numeric<N>
        satisfies Number & Comparable<N> & 
                  Summable<N>
        given N satisfies Numeric<N> {

    doc "The binary |-| operator"
    shared formal N minus(N number);

    doc "The binary |*| operator"
    shared formal N times(N number);

    doc "The binary |/| operator"
    shared formal N divided(N number);

    doc "The binary |**| operator"
    shared formal N power(N number);
    
}

/*shared N plus<X,Y,N>(X x, Y y)
        given N of X|Y satisfies Numeric<N>
        given X satisfies Castable<N> & Numeric<X>
        given Y satisfies Castable<N> & Numeric<Y> {
    return x.as<N>().plus(y.as<N>());
}*/      
