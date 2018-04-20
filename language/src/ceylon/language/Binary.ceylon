/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Abstraction of types that are conceptually a sequence of 
 bits, and may be the subject of bitwise operations. A bit 
 is a [[Boolean]] value. Bits are indexed from right to 
 left, where `0` is the index of the least significant bit.
 
 Binary types support bitwise operations including:
 
 - bitwise complement, written `~b`,
 - bitwise OR, written `a | b`,
 - bitwise AND, written `a & b`,
 - bitwise XOR, and
 - bitshifting operations."
see (class Integer, class Byte)
by ("Stef")
shared interface Binary<Other> of Other 
    given Other satisfies Binary<Other> {

    "The binary complement of this sequence of bits."
    shared formal Other not;

    "Shift the sequence of bits to the left, by the given 
     [[number of places|shift]], filling the least
     significant bits with zeroes (zero extension)."
    shared formal Other leftLogicalShift(Integer shift);

    "Shift the sequence of bits to the right, by the given 
     [[number of places|shift]], filling the most
     significant bits with zeroes (zero extension).
     
     If the sequence of bits represents a signed integer, 
     the sign is not preserved."
    shared formal Other rightLogicalShift(Integer shift);

    "Shift the sequence of bits to the right, by the given 
     [[number of places|shift]], preserving the values of 
     the most significant bits (sign extension).
     
     If the sequence of bits represents a signed integer, 
     the sign is preserved."
    shared formal Other rightArithmeticShift(Integer shift);

    "Performs a logical AND operation.
     
     For any two instances `x` and `y` of a type that 
     implements `Binary`, `x.and(y)` may be written as:
     
         x & y"
    shared formal Other and(Other other);

    "Performs a logical OR operation.
     
     For any two instances `x` and `y` of a type that 
     implements `Binary`, `x.plus(y)` may be written as:
     
         x | y"
    shared formal Other or(Other other);

    "Performs a logical XOR (exclusive or) operation."
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

