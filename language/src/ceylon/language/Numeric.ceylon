"Abstraction of numeric types with addition, 
 `x + y`, subtraction, `x - y`, multiplication, 
 `x * y`, and division, `x / y`, along with 
 additive inverse `-x`.
 
 In general, a numeric type need not define a
 [[total order|Comparable]]. For example, complex 
 numbers do not have a total order. Numeric types 
 with a total order also satisfy [[Scalar]]."
see (`interface Scalar`)
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
    see (`interface Integral`)
    shared formal Other divided(Other other);

}

/*shared N plus<X,Y,N>(X x, Y y)
        given N of X|Y satisfies Numeric<N>
        given X satisfies Castable<N> & Numeric<X>
        given Y satisfies Castable<N> & Numeric<Y> {
    return x.as<N>().plus(y.as<N>());
}*/      
