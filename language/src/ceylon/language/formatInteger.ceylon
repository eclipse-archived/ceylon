/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"The string representation of the given [[integer]] in the 
 base given by [[radix]]. If the given integer is 
 [[negative|Integer.negative]], the string representation 
 will begin with `-`. Digits consist of decimal digits `0` 
 to `9`, together with and lowercase letters `a` to `z` for 
 bases greater than 10.
 
 For example:
 
 - `formatInteger(-46)` is `\"-46\"`
 - `formatInteger(9,2)` is `\"1001\"`
 - `formatInteger(10,8)` is `\"12\"`
 - `formatInteger(511,16)` is `\"1ff\"`
 - `formatInteger(512,32)` is `\"g0\"`"
throws (class AssertionError, 
        "if [[radix]] is not between [[minRadix]] and 
         [[maxRadix]]")
see (function Integer.format)
tagged("Numbers")
deprecated("Use [[Integer.format]]")
shared String formatInteger(
        "The integer value to format."
        Integer integer,
        "The base, between [[minRadix]] and [[maxRadix]] 
         inclusive."
        Integer radix = 10,
        "If not `null`, `groupingSeparator` will be used to
         separate each group of three digits if `radix` is 10,
         or each group of four digits if `radix` is 2 or 16.

         `groupingSeparator` may not be '-', a digit as
         defined by the Unicode general category *Nd*, or a
         letter as defined by the Unicode general categories
         *Lu, Ll, Lt, Lm, and Lo*."
        Character? groupingSeparator = null) {

    assert (minRadix <= radix <= maxRadix);

    if (exists groupingSeparator) {
        "groupingSeparator may not be '-', a digit, or a letter."
        assert (!groupingSeparator.digit
                && !groupingSeparator.letter
                && !groupingSeparator == '-');
    }

    if (integer == 0) {
        return "0";
    }

    value groupingSize =
            if (!groupingSeparator exists)
                then 0
            else if (radix == 10)
                then 3
            else if (radix == 2 || radix == 16)
                then 4
            else 0;

    value groupingChar =
            if (exists groupingSeparator,
                groupingSize != 0)
            then groupingSeparator
            else 'X';

    variable value digitNumber = 0;
    variable {Character*} digits = {};
    variable Integer i = integer < 0 
                         then integer 
                         else -integer;
    while (i != 0) {
        Integer d = -(i % radix);
        Character c;
        if (0<=d<10) {
            c = (d+zeroInt).character;
        }
        else if (10<=d<36) {
            c = (d-10+aIntLower).character;
        }
        else {
            assert (false);
        }
        if (groupingSize != 0
                && groupingSize.divides(digitNumber++)
                && digitNumber != 1) {
            digits = digits.follow(groupingChar);
        }
        digits = digits.follow(c);
        i = (i + d) / radix;
    }
    if (integer < 0) {
        digits = digits.follow('-');
    }
    return String(digits);
}
