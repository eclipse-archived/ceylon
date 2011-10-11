doc "Abstraction of numeric types supporting addition,
     subtraction, multiplication, and division, including
     Natural, Integer, and Float. Additionally, a numeric
     type is expected to define a total order via an 
     implementation of Comparable."
see (Natural, Integer, Float)
by "Gavin"
shared interface Numeric<Other> of Other
        satisfies Number & Comparable<Other> & 
                  Summable<Other>
        given Other satisfies Numeric<Other> {

    doc "The difference between this number and the given 
         number."
    shared formal Other minus(Other other);

    doc "The product of this number and the given number."
    shared formal Other times(Other other);

    doc "The quotient obtained by dividing this number by 
         the given number. For integral numeric types, this 
         operation results in a remainder."
    see (Integral)
    shared formal Other divided(Other other);

    doc "The result of raising this number to the given
         power."
    shared formal Other power(Other other);
    
    doc "The magnitude of this number."
    shared actual formal Other magnitude;
        
    doc "The fractional part of the number, after truncation 
         of the integral part. For integral numeric types,
         the fractional part is always zero."
    shared actual formal Other fractionalPart;
    
    doc "The integral value of the number after truncation 
         of the fractional part. For integral numeric types,
         the integral value of a number is the number 
         itself."
    shared actual formal Other wholePart;

}

/*shared N plus<X,Y,N>(X x, Y y)
        given N of X|Y satisfies Numeric<N>
        given X satisfies Castable<N> & Numeric<X>
        given Y satisfies Castable<N> & Numeric<Y> {
    return x.as<N>().plus(y.as<N>());
}*/      
