"Abstract supertype of types that support scaling by
 a numeric factor `s ** x`. Examples of such types
 include vectors and matrices.
 
 Implementations should generally respect the 
 following constraints, where relevant:
 
 - `x == 1**x`
 
 Implementations of `Scalable` are encouraged to also
 satisfy [[Invertable]], in which case, the following
 identity should be respected:
 
 - `-x == -1**x`
 
 Implementations of `Scalable` usually also satisfy 
 [[Summable]], in which case, the following identity 
 should be respected:
 
 - `x+x == 2**x`"
by ("Gavin")
shared interface Scalable<in Scale, out Value> of Value 
        given Value satisfies Scalable<Scale,Value> 
        given Scale satisfies Numeric<Scale> {
    
    "Scale this value by the given scale factor."
    shared formal Value scale(Scale scalar);
    
}