"An unsigned 8-bit byte. A `Byte` is capable of representing
 integer values between 0 and 255 inclusive. `Byte` is not
 considered a full numeric type, supporting only:
 
 - [[bitwise|Binary]] operations, and 
 - addition modulo 256.
 
 `Byte`s with modular addition form a [[mathematical 
 group|Invertible]]."
shared native final class Byte(bits) 
        extends Object()
        satisfies Binary<Byte> & Invertible<Byte> {
    
    Integer bits;
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
    
    shared actual native Boolean equals(Object that);
    shared actual native Integer hash;
    
    shared actual native String string;
    
}