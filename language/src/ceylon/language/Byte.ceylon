"An unsigned 8-bit byte. A `Byte` is capable of representing
 integer values between 0 and 255 inclusive. `Byte` is not
 considered a full numeric type, supporting only:
 
 - [[bitwise|Binary]] operations, and 
 - addition modulo 256.
 
 `Byte`s with modular addition form a [[mathematical 
 group|Invertible]]. Thus, every byte `b` has an additive
 inverse `-b`, even though `Byte`s are unsigned.
 
 Note that the [[integer]] of the additive inverse of a 
 `Byte` under modular arithmetic is _not_ the same as the 
 additive inverse of the `Byte`'s `integer` under ordinary 
 integer arithmetic. For example:
 
 - `-Byte(1).integer == -1`, but
 - `(-Byte(1)).integer == 255`."
shared native final class Byte(congruent) 
        extends Object()
        satisfies Binary<Byte> & 
                  Invertible<Byte> &
                  Enumerable<Byte> {
    
    "An integer congruent (modulo 256) to the resulting 
     `Byte`.
     
     That is, for any integer `x`:
     
         Byte(x).integer == x % 256 
     
     And for any integers `x` and `y` which are congruent
     modulo 256:
     
         Byte(x) == Byte(y)"
    Integer congruent;
    
    //shared [Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean] bits;
    
    shared native Integer integer;
    
    shared actual native Byte negated;
    shared actual native Byte plus(Byte other);
    
    shared actual native Byte and(Byte other);
    shared actual native Byte flip(Integer index);
    shared actual native Boolean get(Integer index);
    shared actual native Byte leftLogicalShift(Integer shift);
    shared actual native Byte not;
    shared actual native Byte or(Byte other);
    shared actual native Byte rightArithmeticShift(Integer shift);
    shared actual native Byte rightLogicalShift(Integer shift);
    shared actual native Byte set(Integer index, Boolean bit);
    shared actual native Byte xor(Byte other);
    
    shared actual native Byte predecessor;
    shared actual native Byte successor;
    
    shared actual native Byte neighbour(Integer offset);
    shared actual native Integer offset(Byte other);
    shared actual native Integer offsetSign(Byte other);
    
    shared actual native Boolean equals(Object that);
    shared actual native Integer hash;
    
    shared actual native String string;
    
}