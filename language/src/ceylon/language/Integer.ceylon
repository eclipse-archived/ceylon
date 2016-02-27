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
 
 An integer is considered equal to its [[float]] 
 representation, if that exists. That is, for every integer 
 `int`, either `int.float` throws an [[OverflowException]], 
 or the expression `int.float==int` evaluates to `true`.
 
 An integer is represented as a sequence of bits. Not all of 
 the bits in the representation may be addressed by the 
 methods inherited from [[Binary]]:
 
 - For the JVM runtime, the bits at all indices (0 to 63) 
   are addressable.
 - For the JavaScript runtime, the bits at indices 0 to 31 
   are addressable.
 
 Literal integers may be written in decimal, hexadecimal, or
 binary notation:
 
     8660
     #21D4
     $10000111010100
 
 Underscores may be used to group digits:
 
     8660
     #21_D4
     $10_0001_1101_0100"
see (`value runtime.integerSize`,
     `function parseInteger`,
     `function formatInteger`)
tagged("Basic types", "Numbers")
shared native final class Integer(Integer integer)
        extends Object()
        satisfies Integral<Integer> &
                  Binary<Integer> & 
                  Exponentiable<Integer,Integer> {
    
    "The UTF-32 character with this UCS code point."
    throws (`class OverflowException`,
            "if this integer is not in the range 
             `0..#10FFFF` of legal Unicode code points")
    shared native Character character;
    
    shared actual native Boolean divides(Integer other);
    
    shared actual native Integer plus(Integer other);
    shared actual native Integer minus(Integer other);
    shared actual native Integer times(Integer other);
    shared actual native Integer divided(Integer other);
    shared actual native Integer remainder(Integer other);
    shared actual native Integer modulo(Integer modulus);
    
    "The result of raising this number to the given 
     non-negative integer power, where `0^0` evaluates to 
     `1`."
    throws (`class AssertionError`, 
            "if the given [[power|other]] is negative")
    shared actual native Integer power(Integer other);
    
    "Determines if the given object is equal to this `Integer`,
     that is, if:
     
     - the given object is an `Integer` representing the 
       same whole number.
     
     Or if:
     
     - the given object is a [[Float]],
     - its value is neither [[Float.undefined]], nor 
       [[infinity]],
     - the [[fractional part|Float.fractionalPart]] of its 
       value equals `0.0`, and
     - the [[integer part|Float.integer]] part of its value 
       equals this integer, and
     - this integer is between -2<sup>53</sup> and 
       2<sup>53</sup> (exclusive)."
    shared actual native Boolean equals(Object that);
    
    "The hash code of this `Integer`, which is just the
     `Integer` itself, except on the JVM platform where, as 
     with all `hash` codes, this 64-bit `Integer` value is 
     truncated to 32 bits by removal of the 32 higher-order
     bits."
    shared actual native Integer hash => this;
    
    shared actual native Comparison compare(Integer other);
    
    "If the `index` is for an addressable bit, the value of 
     that bit. Otherwise false."
    shared actual native Boolean get(Integer index);
    
    "If the `index` is for an addressable bit, an instance 
     with the same addressable bits as this instance, but 
     with that bit cleared. Otherwise an instance with the 
     same addressable bits as this instance."
    shared actual native Integer clear(Integer index);
    
    "If the `index` is for an addressable bit, an instance 
     with the same addressable bits as this instance, but 
     with that bit flipped. Otherwise an instance with the 
     same addressable bits as this instance."
    shared actual native Integer flip(Integer index);
    
    "If the `index` is for an addressable bit, an instance 
     with the same addressable bits as this instance, but 
     with that bit set to `bit`. Otherwise an instance with 
     the same addressable bits as this instance."
    shared actual native Integer set(Integer index, Boolean bit);
    
    shared actual native Integer not;
    shared actual native Integer or(Integer other);
    shared actual native Integer xor(Integer other);
    shared actual native Integer and(Integer other);
    
    "If shift is in the range of addressable bits 
     (`0..runtime.integerAddressableSize-1`), shift the 
     addressable bits to the right by `shift` positions, 
     with sign extension. Otherwise shift the addressable 
     bits to the right by `(bits + (shift % bits)) % bits` 
     where `bits=runtime.integerAddressableSize`."
    shared actual native Integer rightArithmeticShift(Integer shift);
    
    "If shift is in the range of addressable bits 
     (`0..runtime.integerAddressableSize-1`), shift the 
     addressable bits to the right by `shift` positions, 
     with zero extension. Otherwise shift the addressable 
     bits to the right by `(bits + (shift % bits)) % bits` 
     where `bits=runtime.integerAddressableSize`."
    shared actual native Integer rightLogicalShift(Integer shift);
    
    "If shift is in the range of addressable bits 
     (`0..runtime.integerAddressableSize-1`), shift the 
     addressable bits to the left by `shift` positions.
     Otherwise shift the addressable bits to the left by 
     `(bits + (shift % bits)) % bits` where 
     `bits=runtime.integerAddressableSize`."
    shared actual native Integer leftLogicalShift(Integer shift);
    
    "The number, represented as a [[Float]], if such a 
     representation is possible. 
     
     - Any integer with [[magnitude]] smaller than 
       [[runtime.maxExactIntegralFloat]] (2<sup>53</sup>) 
       has such a representation.
     - For larger integers on the JVM platform, an 
       [[OverflowException]] is thrown."
    throws (`class OverflowException`,
        "if the number cannot be represented as a `Float`
         without loss of precision, that is, if 
         
             this.magnitude>runtime.maxExactIntegralFloat")
    see (`value runtime.maxExactIntegralFloat`)
    shared native Float float;

    "The nearest [[Float]] to this number. 
     
     - For any integer with [[magnitude]] smaller than 
       [[runtime.maxExactIntegralFloat]] (2<sup>53</sup>), 
       this is a `Float` with the exact same mathematical 
       value (and the same value as [[float]]). 
     - For larger integers on the JVM platform, the `Floats` 
       are less dense than the `Integers` so there may be 
       loss of precision.
     
     This method never throws an [[OverflowException]]."
    shared native Float nearestFloat;

    shared actual native Integer predecessor;
    shared actual native Integer successor;
    shared actual native Integer neighbour(Integer offset);
    shared actual native Integer offset(Integer other);
    shared actual native Integer offsetSign(Integer other);
    
    shared actual native Boolean unit;
    shared actual native Boolean zero;
    
    "Determine if this integer is even.
     
     An integer `i` is even if there exists an integer `k` 
     such that:
     
         i == 2*k
     
     Thus, `i` is even if and only if `i%2 == 0`."
    shared native Boolean even => 2.divides(this);
    
    aliased("absolute")
    shared actual native Integer magnitude;    
    shared actual native Integer sign;
    shared actual native Boolean negative;
    shared actual native Boolean positive;
    
    shared actual native Integer wholePart;
    shared actual native Integer fractionalPart;
    
    shared actual native Integer negated;
    
    shared actual native Integer timesInteger(Integer integer);    
    shared actual native Integer plusInteger(Integer integer);
    
    "The result of raising this number to the given 
     non-negative integer power, where `0^0` evaluates to 
     `1`."
    throws (`class AssertionError`, 
        "if the given [[power|integer]] is negative")
    shared actual native Integer powerOfInteger(Integer integer);
    
    see (`function formatInteger`)
    shared actual native String string;

    shared actual native Boolean largerThan(Integer other); 
    shared actual native Boolean smallerThan(Integer other); 
    shared actual native Boolean notSmallerThan(Integer other); 
    shared actual native Boolean notLargerThan(Integer other); 
    
    "A [[Byte]] whose [[signed|Byte.signed]] and
     [[unsigned|Byte.unsigned]] interpretations are 
     congruent modulo 256 to this integer."
    shared native Byte byte;
    
}
