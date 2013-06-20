"Abstraction of numeric types supporting addition,
 subtraction, multiplication, and division, including
 `Integer` and `Float`. Additionally, a numeric type 
 is expected to define a total order via an 
 implementation of `Comparable`."
//see (`Integer`, `Float`, `Comparable`)
by ("Gavin")
shared interface Numeric<Other> of Other
        satisfies Summable<Other> & Invertable<Other>
        given Other satisfies Numeric<Other> {

    "The difference between this number and the given 
     number."
    shared formal Other minus(Other other);

    "The product of this number and the given number."
    shared formal Other times(Other other);

    "The quotient obtained by dividing this number by 
     the given number. For integral numeric types, this 
     operation results in a remainder."
    //see (`Integral`)
    shared formal Other divided(Other other);

}

/*shared N plus<X,Y,N>(X x, Y y)
        given N of X|Y satisfies Numeric<N>
        given X satisfies Castable<N> & Numeric<X>
        given Y satisfies Castable<N> & Numeric<Y> {
    return x.as<N>().plus(y.as<N>());
}*/      
