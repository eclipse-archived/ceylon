doc "A 64-bit integer (or the closest approximation to a 
     64-bit integer provided by the underlying platform)."
shared abstract class Integer()
        extends Object()
        satisfies Scalar<Integer> & 
                  Integral<Integer> &
                  Binary<Integer> & 
                  Exponentiable<Integer,Integer> {
    
    doc "The UTF-32 character with this UCS code point."
    shared formal Character character;
}

shared native Integer maxIntegerValue;
shared native Integer minIntegerValue;