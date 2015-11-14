"Abstraction of types that are conceptually a sequence of 
 bits, and may be the subject of bitwise operations. A bit 
 is a [[Boolean]] value. Bits are indexed from right to 
 left, where `0` is the index of the least significant bit."
see (`class Integer`)
by ("Stef")
shared interface Binary<Other> of Other 
    given Other satisfies Binary<Other> {

    "The binary complement of this sequence of bits."
    shared formal Other not;

    "Shift the sequence of bits to the left, by the 
     given [[number of places|shift]], filling the least
     significant bits with zeroes."
    shared formal Other leftLogicalShift(Integer shift);

    "Shift the sequence of bits to the right, by the 
     given [[number of places|shift]], filling the most
     significant bits with zeroes.
     
     If the sequence of bits represents a signed integer, 
     the sign is not preserved."
    shared formal Other rightLogicalShift(Integer shift);

    "Shift the sequence of bits to the right, by the 
     given [[number of places|shift]], preserving the values
     of the most significant bits.
     
     If the sequence of bits represents a signed integer, 
     the sign is preserved."
    shared formal Other rightArithmeticShift(Integer shift);

    "Performs a logical AND operation."
    shared formal Other and(Other other);

    "Performs a logical inclusive OR operation."
    shared formal Other or(Other other);

    "Performs a logical exclusive OR operation."
    shared formal Other xor(Other other);

    "Retrieves a given bit from this bit sequence if 
     `0 <= index < size`, otherwise returns false."
    shared formal Boolean get(Integer index);

    "Returns an instance with the given bit set to the given 
     value if `0 <= index < size`, otherwise returns a value 
     with the same bits as this value."
    shared formal Other set(Integer index, Boolean bit = true);

    "Returns an instance with the given bit set to 0 if 
     `0 <= index < size`, otherwise returns a value with the 
     same bits as this value."
    shared default Other clear(Integer index) 
            => set(index, false);

    "Returns an instance with the given bit flipped to its 
     opposite value if `0 <= index < size`, otherwise 
     returns a value with the same bits as this value."
    shared formal Other flip(Integer index);
}

