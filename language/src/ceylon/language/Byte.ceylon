"An 8-bit byte. A `Byte` value represents a congruence class
 of [[integers|Integer]] modulo 256, and may be interpreted 
 as:
 
 - an [[unsigned]] integer value in the range `0..255`, or 
 - a [[signed]] integer value in the range `-128..127`. 
 
 `Byte` is not considered a full numeric type, supporting 
 only:
 
 - [[bitwise|Binary]] operations, and 
 - addition and subtraction modulo 256.
 
 `Byte`s with modular addition form a [[mathematical 
 group|Invertible]]. Thus, every byte `b` has an additive
 inverse `-b` where:
 
     (-b).signed == -b.signed
     (-b).unsigned == b.unsigned==0 then 0 else 256 - b.unsigned
 
 `Byte` is a [[recursive enumerable type|Enumerable]]. For
 example, the range:
 
     254.byte .. 1.byte
     
 contains the values `254.byte, 255.byte, 0.byte, 1.byte`.
 
 `Byte` does not have a [[total order|Comparable]] because
 any such order would:
  
 - be inconsistent with the definition of [[successor]] and 
   [[predecessor]] under modular addition, and
 - would depend on interpretation of the `Byte` value as
   signed or unsigned.
 
 Thus, to compare the magnitude of two bytes, it is 
 necessary to first convert them to either their `signed` or 
 `unsigned` integer values.
 
 `Byte`s are useful mainly because they can be efficiently 
 stored in an [[Array]]."
tagged("Basic types")
shared native final class Byte(congruent) 
        extends Object()
        satisfies Binary<Byte> & 
                  Invertible<Byte> &
                  Enumerable<Byte> {
    
    "An integer member of the congruence class of the 
     resulting `Byte`.
     
     For any integer `x>=0`:
     
         x.byte.unsigned == x % 256
         x.byte.signed == x % 256
     
     And for an integer `x<0`:
     
         x.byte.unsigned == 256 + x % 256
         x.byte.signed == x % 256
     
     And for any integers `x` and `y` which are congruent
     modulo 256:
     
         x.byte == y.byte"
    Integer congruent;
    
    //shared [Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean] bits;
    
    "Whether this byte is even."
    shared native Boolean even;
    
    "Whether this byte is zero."
    shared native Boolean zero;
    
    "Whether this byte is one."
    shared native Boolean unit;
    
    "This byte interpreted as an unsigned integer in the
     range `0..255`."
    shared native Integer unsigned;
    
    "This byte interpreted as a signed integer in the range 
     `-128..127`."
    shared native Integer signed;
    
    "The additive inverse of this byte. For any integer `x`:
     
         (-x.byte).signed = -x.byte.signed"
    shared actual native Byte negated;
    
    "The modulo 256 sum of this byte and the given byte."
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
    
    "The [[unsigned]] interpretation of this byte as a 
     string."
    shared actual native String string;
    
}