"Abstraction of numeric types like `Integer` that may be 
 represented as a sequence of bits, and may be the subject
 of bitwise operations."
see (Integer)
by ("Stef")
shared interface Binary<Other> of Other 
    given Other satisfies Binary<Other> {

    "The binary complement of this sequence of bits."
    shared formal Other not;

    "The number of bits (0 or 1) that this sequence of bits can hold."
    shared formal Integer size;

    "Performs a left logical shift. Sign is not preserved. Padded with zeros."
    shared formal Other leftLogicalShift(Integer shift);

    "Performs a right logical shift. Sign is not preserved. Padded with zeros."
    shared formal Other rightLogicalShift(Integer shift);

    "Performs a right arithmetic shift. Sign is preserved. Padded with zeros."
    shared formal Other rightArithmeticShift(Integer shift);

    "Performs a logical AND operation."
    shared formal Other and(Other other);

    "Performs a logical inclusive OR operation."
    shared formal Other or(Other other);

    "Performs a logical exclusive OR operation."
    shared formal Other xor(Other other);

    "Retrieves a given bit from this bit sequence. Bits are indexed from
     right to left."
    shared formal Boolean get(Integer index);

    "Returns a new number with the given bit set to the given value.
     Bits are indexed from right to left."
    shared formal Other set(Integer index, Boolean bit = true);

    "Returns a new number with the given bit set to 0.
     Bits are indexed from right to left."
    shared default Other clear(Integer index){
        return set(index, false);
    }

    "Returns a new number with the given bit flipped to its opposite value.
     Bits are indexed from right to left."
    shared formal Other flip(Integer index);
}

