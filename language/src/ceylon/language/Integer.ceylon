doc "A 64-bit integer, or the closest approximation to a 
     64-bit integer provided by the underlying platform.
     
     - For the JVM runtime, integer values between
       -2<sup>63</sup> and 2<sup>63</sup>-1 may be 
       represented without overflow.
     - For the JavaScript runtime, integer values with a
       magnitude no greater than 2<sup>53</sup> may be
       represented without loss of precision.
     
     Overflow or loss of precision occurs silently (with
     no exception raised)."
shared abstract class Integer()
        extends Object()
        satisfies Scalar<Integer> & 
                  Integral<Integer> &
                  Binary<Integer> & 
                  Exponentiable<Integer,Integer> {
    
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