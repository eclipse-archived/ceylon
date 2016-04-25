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
    
    "This byte interpreted as an unsigned integer in the
     range `0..255` (that is, `0..#FF`)."
    shared native Integer unsigned 
            = (congruent % #100).and(#FF);
    
    "This byte interpreted as a signed integer in the range 
     `-128..127` (that is, `-#80..#7F`)."
    shared native Integer signed => unsigned - #80;
    
    //shared Boolean[8] bits;
    
    "Whether this byte is even."
    shared native Boolean even => and(1.byte).zero;
    
    "Whether this byte is zero."
    shared native Boolean zero => this == 0.byte;
    
    "Whether this byte is one."
    shared native Boolean unit => this == 1.byte;
    
    "The additive inverse of this byte. For any integer `x`:
     
         (-x.byte).signed = -x.byte.signed"
    shared actual native Byte negated => (-signed).byte;
    
    "The modulo 256 sum of this byte and the given byte."
    shared actual native Byte plus(Byte other)
            => (this.signed + other.signed).byte;
    
    shared actual native Boolean get(Integer index) 
            => 0 <= index <= 7
            then !and(1.byte.leftLogicalShift(index)).zero 
            else false;
    
    shared actual native Byte flip(Integer index)
            => 0 <= index <= 7
            then xor(1.byte.leftLogicalShift(index)) 
            else this;
    
    shared actual native Byte set(Integer index, Boolean bit)
            => 0 <= index <= 7
            then (bit then or(1.byte.leftLogicalShift(index)) 
                      else and(1.byte.leftLogicalShift(index).not))
            else this;
    
    shared actual native Byte clear(Integer index) 
            => 0 <= index <= 7
            then and(1.byte.leftLogicalShift(index).not) 
            else this;
    
    shared actual native Byte not 
            => (signed.not).byte;
    
    shared actual native Byte and(Byte other) 
            => (signed.and(other.signed)).byte;
    
    shared actual native Byte or(Byte other)
            => (signed.or(other.signed)).byte;
    
    shared actual native Byte xor(Byte other)
            => (signed.xor(other.signed)).byte;
    
    shared actual native Byte leftLogicalShift(Integer shift)
            => signed.leftLogicalShift(shift).byte;
    
    shared actual native Byte rightArithmeticShift(Integer shift)
            => signed.rightArithmeticShift(shift).byte;
    
    shared actual native Byte rightLogicalShift(Integer shift)
            => unsigned.rightLogicalShift(shift).byte;
    
    shared actual native Byte predecessor => (signed-1).byte;
    shared actual native Byte successor => (signed+1).byte;
    
    shared actual native Byte neighbour(Integer offset) 
            => (signed + offset).byte;
    
    shared actual native Integer offset(Byte other)
            => minus(other).unsigned;
    
    shared actual native Integer offsetSign(Byte other)
            => this==other then 0 else 1;
    
    shared actual native Boolean equals(Object that) 
            => if (is Byte that) 
            then this.signed == that.signed
            else false;
    
    shared actual native Integer hash => signed;
    
    "The [[unsigned]] interpretation of this byte as a 
     string."
    shared actual native String string => unsigned.string;
    
}