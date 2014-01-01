"An exact representation of a positive whole number, 
 negative whole number, or zero. The largest and smallest 
 representable values are platform-dependent:
 
 - For the JVM runtime, integer values between
   -2<sup>63</sup> and 2<sup>63</sup>-1 may be represented 
   without overflow.
 - For the JavaScript runtime, integer values with a
   magnitude no greater than 2<sup>53</sup> may be
   represented without loss of precision.
 
 Overflow or loss of precision occurs silently (with no 
 exception raised)."
see (`value runtime`)
shared native final class Integer(integer)
        extends Object()
        satisfies Integral<Integer> &
                  Binary<Integer> & 
                  Exponentiable<Integer,Integer> {
    
    "The UTF-32 character with this UCS code point."
    throws (`class OverflowException`,
            "if there is no such character")
    shared native Character character;
    
    shared actual native Integer not;
    shared actual native Integer or(Integer other);
    shared actual native Integer xor(Integer other);
    shared actual native Integer and(Integer other);
    
    shared actual native Integer rightArithmeticShift(Integer shift);    
    shared actual native Integer rightLogicalShift(Integer shift);
    shared actual native Integer leftLogicalShift(Integer shift);
    
    shared actual native Integer plus(Integer other);
    shared actual native Integer minus(Integer other);
    shared actual native Integer times(Integer other);
    shared actual native Integer divided(Integer other);
    shared actual native Integer remainder(Integer other);
    shared actual native Integer power(Integer other);
    
    shared actual native Boolean equals(Object that);
    shared actual native Integer hash;
    shared actual native Comparison compare(Integer other);
    
    shared actual native Integer size;
    shared actual native Boolean get(Integer index);
    shared actual native Integer flip(Integer index);
    shared actual native Integer set(Integer index, Boolean bit);
    
    shared actual native Float float;
    shared actual Integer integer;
    
    shared actual native Integer predecessor;
    shared actual native Integer successor;
    shared actual native Integer integerValue;
    
    shared actual native Boolean unit;
    shared actual native Boolean zero;
    
    shared actual native Integer magnitude;    
    shared actual native Integer sign;
    shared actual native Boolean negative;
    shared actual native Boolean positive;
    
    shared actual native Integer wholePart;
    shared actual native Integer fractionalPart;
    
    shared actual native Integer positiveValue;
    shared actual native Integer negativeValue;
    
}
