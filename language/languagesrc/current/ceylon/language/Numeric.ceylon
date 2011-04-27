shared interface Numeric<N>
        satisfies Number & Comparable<N> & 
                  Summable<N> & Castable<N>
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

shared N plus<N>(Castable<N> x, Castable<N> y)
        given N satisfies Numeric<N> {
    return x.as<N>().plus(y.as<N>());
}       
