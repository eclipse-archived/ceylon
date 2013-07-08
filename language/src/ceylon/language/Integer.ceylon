"A 64-bit integer, or the closest approximation to a 
 64-bit integer provided by the underlying platform.
 
 - For the JVM runtime, integer values between
   -2<sup>63</sup> and 2<sup>63</sup>-1 may be 
   represented without overflow.
 - For the JavaScript runtime, integer values with a
   magnitude no greater than 2<sup>53</sup> may be
   represented without loss of precision.
 
 Overflow or loss of precision occurs silently (with
 no exception raised)."
shared native final class Integer(Integer integer)
        extends Object()
        satisfies Scalar<Integer> & 
                  Integral<Integer> &
                  Binary<Integer> & 
                  Exponentiable<Integer,Integer> {
    
    "The UTF-32 character with this UCS code point."
    shared native Character character;
}

"The maximum `Integer` value that can be represented
 by the backend"
shared native Integer maxIntegerValue;

"The minimum `Integer` value that can be represented
 by the backend"
shared native Integer minIntegerValue;
