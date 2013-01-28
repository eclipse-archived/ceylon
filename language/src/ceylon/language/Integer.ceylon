doc "A 64-bit integer (or the closest approximation to a 
     64-bit integer provided by the underlying platform)."
shared abstract class Integer()
        extends Object()
        satisfies Scalar<Integer> & 
                  Integral<Integer> &
                  Binary<Integer> & 
                  Exponentiable<Integer,Integer> &
                  Castable<Integer|Float> {
    
    doc "The UTF-32 character with this UCS code point."
    shared formal Character character;
}

doc "The `Integer` value of the given string representation 
     of an integer, or `null` if the string does not represent 
     an integer or if the mathematical integer it represents 
     is too large in magnitude to be represented by an 
     `Integer`.
     
     The syntax accepted by this method is the same as the 
     syntax for an `Integer` literal in the Ceylon language 
     except that it may optionally begin with a sign 
     character (`+` or `-`)."
shared native Integer? parseInteger(String string);