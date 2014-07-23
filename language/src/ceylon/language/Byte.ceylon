"An 8-bit byte. A `Byte` value may be interpreted either as:
 
 - an [[unsigned]] integer value in the range `0..255`, or 
 - a [[signed]] integer value in the range `-128..127`. 
 
 `Byte` is not considered a full numeric type, supporting 
 only:
 
 - [[bitwise|Binary]] operations, and 
 - addition modulo 256.
 
 `Byte`s with modular addition form a [[mathematical 
 group|Invertible]]. Thus, every byte `b` has an additive
 inverse `-b` where:
 
     (-b).signed == -b.signed
     (-b).unsigned == b.unsigned==0 then 0 else 256 - b.unsigned
 
 `Byte` is a [[recursive enumerable type|Enumerable]]. For
 example, the range:
 
     Byte(254)..Byte(1)
     
 contains the values `Byte(254), Byte(255), Byte(0), Byte(1)`.
 
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
shared native final class Byte(congruent) 
        extends Object()
        satisfies Binary<Byte> & 
                  Invertible<Byte> &
                  Enumerable<Byte> {
    
    "An integer congruent (modulo 256) to the resulting 
     `Byte`.
     
     That is, for any integer `x>=0`:
     
         Byte(x).unsigned == x % 256
         Byte(x).signed == x % 256
     
     And for an integer `x<0`:
     
         Byte(x).unsigned == 256 + x % 256
         Byte(x).signed == x % 256
     
     And for any integers `x` and `y` which are congruent
     modulo 256:
     
         Byte(x) == Byte(y)"
    Integer congruent;
    
    //shared [Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean] bits;
    
    "This byte interpreted as an unsigned integer in the
     range `0..255`."
    shared native Integer unsigned;
    
    "This byte interpreted as a signed integer in the range 
     `-128..127`."
    shared native Integer signed;
    
    "The additive inverse of this byte. For any integer `x`:
     
         (-Byte(x)).signed = -Byte(x).signed"
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