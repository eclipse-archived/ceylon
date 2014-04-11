"Abstraction of types that are conceptually a sequence of bits, 
 and may be the subject of bitwise operations. 
 A bit is a [[Boolean]] value."
see (`class Integer`)
by ("Stef")
shared interface Binary<Other> of Other 
    given Other satisfies Binary<Other> {

    "The binary complement of this sequence of bits."
    shared formal Other not;

    "Performs a left logical shift. Sign is not preserved. 
     Padded with zeros."
    shared formal Other leftLogicalShift(Integer shift);

    "Performs a right logical shift. Sign is not preserved. 
     Padded with zeros."
    shared formal Other rightLogicalShift(Integer shift);

    "Performs a right arithmetic shift. Sign is preserved. 
     Padded with zeros."
    shared formal Other rightArithmeticShift(Integer shift);

    "Performs a logical AND operation."
    shared formal Other and(Other other);

    "Performs a logical inclusive OR operation."
    shared formal Other or(Other other);

    "Performs a logical exclusive OR operation."
    shared formal Other xor(Other other);

    "Retrieves a given bit from this bit sequence 
     if `0 <= index < size`, otherwise returns false. 
     Bits are indexed from right to left."
    shared formal Boolean get(Integer index);

    "Returns an instance with the given bit set to the given 
     value if `0 <= index < size`, otherwise returns an instance
     with the same bits set as this instance. 
     Bits are indexed from right to left."
    shared formal Other set(Integer index, Boolean bit = true);

    "Returns an instance with the given bit set to 0
     if `0 <= index < size`, otherwise returns an instance 
     with the same bits set as this instance.
     Bits are indexed from right to left."
    shared default Other clear(Integer index) 
            => set(index, false);

    "Returns an instance with the given bit flipped to its 
     opposite value if `0 <= index < size`, otherwise returns an instance 
     with the same bits set as this instance. 
     Bits are indexed from right to left."
    shared formal Other flip(Integer index);
}

