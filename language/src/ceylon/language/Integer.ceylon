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
    
    shared actual native Integer negated;
    
    shared actual native Integer plus(Integer other);
    shared actual native Integer minus(Integer other);
    shared actual native Integer times(Integer other);
    shared actual native Integer divided(Integer other);
    shared actual native Integer remainder(Integer other);
    shared actual native Integer modulo(Integer modulus);
    
    "Determines if this integer is a factor of the given 
     integer, that is, if `remainder(other)==0 `."
    shared actual native Boolean divides(Integer other)
            => other % this == 0;
    
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
     truncated to 32 bits by taking the exclusive 
     disjunction of the 32 lowest-order bits with the 32 
     highest-order bits, that is:
     
         int.hash == int.rightArithmeticShift(32).exclusiveOr(int)"
    shared actual native Integer hash => this;
    
    "Compare this integer with the given integer, returning:
     
     - `smaller`, if `this < other`,
     - `larger`, if `this > other`, or
     - `equal`, if `this == other`."
    shared actual native Comparison compare(Integer other)
            =>   if (this < other) then smaller
            else if (this > other) then larger
            else equal;
    
    function indexInRange(Integer index)
            => 0 <= index < runtime.integerAddressableSize;
    
    function mask(Integer index) 
            => 1.leftLogicalShift(index);
    
    "If the `index` is for an addressable bit, the value of 
     that bit. Otherwise false."
    shared actual native Boolean get(Integer index) 
            => indexInRange(index)
            then and(mask(index)) != 0 
            else false;
    
    "If the `index` is for an addressable bit, an instance 
     with the same addressable bits as this instance, but 
     with that bit cleared. Otherwise an instance with the 
     same addressable bits as this instance."
    shared actual native Integer clear(Integer index) 
            => indexInRange(index)
            then and(mask(index).not) 
            else this;
    
    "If the `index` is for an addressable bit, an instance 
     with the same addressable bits as this instance, but 
     with that bit flipped. Otherwise an instance with the 
     same addressable bits as this instance."
    shared actual native Integer flip(Integer index)
            => indexInRange(index)
            then xor(mask(index)) 
            else this;
    
    "If the `index` is for an addressable bit, an instance 
     with the same addressable bits as this instance, but 
     with that bit set to `bit`. Otherwise an instance with 
     the same addressable bits as this instance."
    shared actual native Integer set(Integer index, Boolean bit)
            => indexInRange(index)
            then (bit then or(mask(index)) 
                      else and(mask(index).not))
            else this;
    
    shared actual native Integer not;
    shared actual native Integer or(Integer other);
    shared actual native Integer xor(Integer other);
    shared actual native Integer and(Integer other);
    
    "If the given [[shift]] is in the range of 
     [[addressable bits|runtime.integerAddressableSize]]
     given by
     
         0..runtime.integerAddressableSize-1
     
     shift the addressable bits to the right by `shift` 
     positions, using sign extension to fill in the most
     significant bits. Otherwise shift the 
     addressable bits to the right by 
     
         shift.and(runtime.integerAddressableSize-1)
     
     positions, using sign extension."
    shared actual native Integer rightArithmeticShift(Integer shift);
    
    "If the given [[shift]] is in the range of 
     [[addressable bits|runtime.integerAddressableSize]]
     given by
     
         0..runtime.integerAddressableSize-1
     
     shift the addressable bits to the right by `shift` 
     positions, using zero extension to fill in the most
     significant bits. Otherwise shift the addressable bits 
     to the right by 
     
         shift.and(runtime.integerAddressableSize-1)
     
     positions, using zero extension."
    aliased ("rightShift")
    shared actual native Integer rightLogicalShift(Integer shift);
    
    "If the given [[shift]] is in the range of 
     [[addressable bits|runtime.integerAddressableSize]]
     given by
     
         0..runtime.integerAddressableSize-1
     
     shift the addressable bits to the left by `shift` 
     positions, using zero extension to fill in the least
     significant bits. Otherwise shift the addressable bits 
     to the left by 
     
         shift.and(runtime.integerAddressableSize-1)
     
     positions, using zero extension."
    aliased ("leftShift")
    shared actual native Integer leftLogicalShift(Integer shift);
    
    "The number, represented as a [[Float]], if such a 
     representation is possible. 
     
     - Any integer with [[magnitude]] smaller than 
       [[runtime.maxExactIntegralFloat]] (2<sup>53</sup>) 
       has such a representation.
     - For larger integers on the JVM platform, an 
       [[OverflowException]] is thrown. If this behavior is 
       not desired, use [[nearestFloat]] instead."
    throws (`class OverflowException`,
        "if the number cannot be represented as a `Float`
         without loss of precision, that is, if 
         
             this.magnitude>runtime.maxExactIntegralFloat
         
         (Note that [[nearestFloat]] does not produce an
         exception in this case.)")
    see (`value runtime.maxExactIntegralFloat`,
         `value nearestFloat`)
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
    see (`value float`)
    shared native Float nearestFloat;

    shared actual native Integer predecessor => minus(1);
    shared actual native Integer successor => plus(1);
    
    shared actual native Integer neighbour(Integer offset)
            => plus(offset);
    
    shared actual native Integer offset(Integer other)
            => minus(other);
    
    shared actual native Integer offsetSign(Integer other)
            => super.offsetSign(other);
    
    shared actual native Boolean unit => this==1;
    shared actual native Boolean zero => this==0;
    
    "Determine if this integer is even.
     
     An integer `i` is even if there exists an integer `k` 
     such that:
     
         i == 2*k
     
     Thus, `i` is even if and only if `i%2 == 0`."
    shared native Boolean even => 2.divides(this);
    
    aliased("absolute")
    shared actual native Integer magnitude 
            => this < 0 then -this else this;
        
    shared actual native Integer sign
            =>   if (this < 0) then -1
            else if (this > 0) then 1
            else 0;
        
    shared actual native Boolean negative => this < 0;
    shared actual native Boolean positive => this > 0;
    
    shared actual native Integer wholePart => this;
    shared actual native Integer fractionalPart => 0;
    
    shared actual native Integer timesInteger(Integer integer)
            => times(integer);
        
    shared actual native Integer plusInteger(Integer integer)
            => plus(integer);
    
    "The result of raising this number to the given 
     non-negative integer power, where `0^0` evaluates to 
     `1`."
    throws (`class AssertionError`, 
        "if the given [[power|integer]] is negative")
    shared actual native Integer powerOfInteger(Integer integer)
            => power(integer);
    
    see (`function formatInteger`)
    shared actual native String string;

    shared actual native Boolean largerThan(Integer other); 
    shared actual native Boolean smallerThan(Integer other); 
    shared actual native Boolean notSmallerThan(Integer other); 
    shared actual native Boolean notLargerThan(Integer other); 
    
    "A [[Byte]] whose [[signed|Byte.signed]] and
     [[unsigned|Byte.unsigned]] interpretations are 
     congruent modulo 256 to this integer."
    shared native Byte byte => Byte(this);
    
}
