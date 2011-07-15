shared interface Numeric<Other> of Other
        satisfies Number & Comparable<Other> & 
                  Summable<Other>
        given Other satisfies Numeric<Other> {

    doc "The binary |-| operator"
    shared formal Other minus(Other number);

    doc "The binary |*| operator"
    shared formal Other times(Other number);

    doc "The binary |/| operator"
    shared formal Other divided(Other number);

    doc "The binary |**| operator"
    shared formal Other power(Other number);
    
    doc "The magnitude of the number"
    shared actual formal Other magnitude;
    
    doc "1 if the number is positive, -1 if it
         is negative, or 0 if it is zero."
    shared actual formal Other sign;
    
    doc "The fractional part of the number,
         after truncation of the integral
         part"
    shared actual formal Other fractionalPart;
    
    doc "The integral value of the number 
         after truncation of the fractional
         part"
    shared actual formal Other wholePart;

}

/*shared N plus<X,Y,N>(X x, Y y)
        given N of X|Y satisfies Numeric<N>
        given X satisfies Castable<N> & Numeric<X>
        given Y satisfies Castable<N> & Numeric<Y> {
    return x.as<N>().plus(y.as<N>());
}*/      
