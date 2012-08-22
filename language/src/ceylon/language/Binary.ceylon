doc "Abstraction of numeric types that consist in
     a sequence of bits, like `Integer`."
see (Integer)
by "Stef"
shared interface Binary<Other> of Other 
    given Other satisfies Binary<Other> {

    doc "The binary complement of this sequence of bits."
    shared formal Other not;

    doc "The number of bits (0 or 1) that this sequence of bits can hold."
    shared formal Integer size;

    doc "Performs a left logical shift. Sign is not preserved. Padded with zeros."
    shared formal Other leftLogicalShift(Integer shift);

    doc "Performs a right logical shift. Sign is not preserved. Padded with zeros."
    shared formal Other rightLogicalShift(Integer shift);

    doc "Performs a right arithmetic shift. Sign is preserved. Padded with zeros."
    shared formal Other rightArithmeticShift(Integer shift);

    doc "Performs a logical AND operation."
    shared formal Other and(Other other);

    doc "Performs a logical inclusive OR operation."
    shared formal Other or(Other other);

    doc "Performs a logical exclusive OR operation."
    shared formal Other xor(Other other);

    doc "Retrieves a given bit from this bit sequence. Bits are indexed from
         right to left."
    shared formal Boolean get(Integer index);

    doc "Returns a new number with the given bit set to the given value.
         Bits are indexed from right to left."
    shared formal Other set(Integer index, Boolean bit = true);

    doc "Returns a new number with the given bit set to 0.
         Bits are indexed from right to left."
    shared default Other clear(Integer index){
        return set(index, false);
    }

    doc "Returns a new number with the given bit flipped to its opposite value.
         Bits are indexed from right to left."
    shared formal Other flip(Integer index);
}

