"Abstract supertype of types that support scaling by a 
 numeric factor `s ** x`. Examples of such types include 
 vectors and matrices.
 
 Concrete classes which implement this interface should
 satisfy:
 
 - `x == 1**x
 - `a ** (b ** x) == a*b ** x`
 
 where `1` denotes the multiplicative identity of the 
 numeric scaling type.
 
 Concrete classes which implement this interface are
 encouraged to also satisfy [[Invertable]], in which case, 
 the following identity should be satisfied:
 
 - `-x == -1**x`
 
 Concrete classes which implement this interface and which
 also satisfy [[Summable]] should satisfy:
 
 - `x+x == 2**x`
 - `a ** (x+y) == a**x + a**y`"
by ("Gavin")
shared interface Scalable<in Scale, out Value> of Value 
        given Value satisfies Scalable<Scale,Value> 
        given Scale satisfies Numeric<Scale> {
    
    "Scale this value by the given scale factor."
    shared formal Value scale(Scale scalar);
    
}