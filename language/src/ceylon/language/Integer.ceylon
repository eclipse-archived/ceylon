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
 exception raised).
 
 Not all of the bits in the representation may be addressed by the 
 methods inherited from [[Binary]]:
 
 - For the JVM runtime, the bits at all indices (0 to 63) are 
   addressable.
 
 - For the JavaScript runtime, the bits at indices 0 to 31 are 
   addressable.
 "
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
    
    "If shift is in the range of addressable bits 
     (`0..runtime.integerAddressableSize-1`), shift the addressable bits to 
     the right by `shift` positions, with sign extension.
     Otherwise shift the addressable bits to the right by 
     `(bits + (shift % bits)) % bits` 
     where `bits=runtime.integerAddressableSize`"
    shared actual native Integer rightArithmeticShift(Integer shift);
    "If shift is in the range of addressable bits 
     (`0..runtime.integerAddressableSize-1`), shift the addressable bits to 
     the right by `shift` positions, with zero extension.
     Otherwise shift the addressable bits to the right by 
     `(bits + (shift % bits)) % bits` 
     where `bits=runtime.integerAddressableSize`"
    shared actual native Integer rightLogicalShift(Integer shift);
    "If shift is in the range of addressable bits 
     (`0..runtime.integerAddressableSize-1`), shift the addressable bits to 
     the left by `shift` positions.
     Otherwise shift the addressable bits to the left by 
     `(bits + (shift % bits)) % bits` 
     where `bits=runtime.integerAddressableSize`"
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
    
    "If the `index` is for an addressable bit, the value of that bit.
     Otherwise false."
    shared actual native Boolean get(Integer index);
    "If the `index` is for an addressable bit, an instance with the same 
     addressable bits as this instance, but with that bit cleared.
     Otherwise an instance with the same addressable bits as this instance."
    shared actual native Integer clear(Integer index);
    "If the `index` is for an addressable bit, an instance with the same 
     addressable bits as this instance, but with that bit flipped.
     Otherwise an instance with the same addressable bits as this instance."
    shared actual native Integer flip(Integer index);
    "If the `index` is for an addressable bit, an instance with the same 
     addressable bits as this instance, but with that bit set to `bit`.
     Otherwise an instance with the same addressable bits as this instance."
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
    
    shared actual native Integer negated;
    
    shared actual native Integer timesInteger(Integer integer);    
    shared actual native Integer plusInteger(Integer integer);
    
    shared actual native String string;
}
