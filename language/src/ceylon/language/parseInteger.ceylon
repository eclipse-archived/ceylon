/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"The [[Integer]] value of the given 
 [[string representation|string]] of an integer value in the 
 base given by [[radix]], or `null` if the string does not 
 represent an integer in that base, or if the mathematical 
 integer it represents is too large in magnitude to be 
 represented by an instance of the class `Integer`.
 
 The syntax accepted by this function depends upon the 
 given [[base|radix]]:
 
 - For base 10, the accepted syntax is the same as the 
   syntax for an `Integer` literal in the Ceylon language 
   except that it may optionally begin with a sign character 
   (`+` or `-`) and may not contain grouping underscore 
   characters. That is, an optional sign character, followed
   be a string of decimal digits, followed by an optional SI
   magnitude: `k`, `M`, `G`, `T`, or `P`.
 - For other bases, the accepted syntax is an optional sign
   character, followed by a string of digits of the given
   base. 
 
 The given `radix` specifies the base of the string 
 representation. The list of available digits starts from 
 `0` to `9`, followed by `a` to `z`. When parsing in a 
 specific base, the first `radix` digits from the available 
 digits list is used. This function is not case sensitive; 
 `a` and `A` both correspond to the digit `a` whose decimal 
 value is `10`.
 
     Integer: Base10 | BaseN
     Base10: Sign? Base10Digits Magnitude
     BaseN: Sign? BaseNDigits
     Sign: '+' | '-'
     Magnitude: 'k' | 'M' | 'G' | 'T' | 'P'
     Base10Digits: ('0'..'9')+
     BaseNDigits: ('0'..'9'|'a'..'z'|'A'..'Z')+"
throws (class AssertionError, 
        "if [[radix]] is not between [[minRadix]] and 
         [[maxRadix]]")
see (function Integer.parse)
tagged("Numbers", "Basic types")
shared Integer? parseInteger(
            "The string representation to parse."
            String? string,
            "The base, between [[minRadix]] and [[maxRadix]] 
             inclusive."
            Integer radix = 10) 
        => if (exists string, 
               is Integer result 
                    = parseIntegerInternal(string, radix))
        then result 
        else null;

Integer minRadix = 2;
Integer maxRadix = 36;

Integer|ParseException parseIntegerInternal(
    String string, Integer radix = 10) {
    
    "illegal radix"
    assert (minRadix <= radix <= maxRadix); 
    
    Integer start;
    Integer max = runtime.minIntegerValue / radix;
    
    // Parse the sign
    Boolean negative;
    if (exists char = string.first) {
        if (char == '-') {
            negative = true;
            start = 1;
        }
        else if (char == '+') {
            negative = false;
            start = 1;
        }
        else {
            negative = false;
            start = 0;
        }
    }
    else {
        return ParseException("illegal format for Integer: no digits");
    }
    
    value limit = negative 
            then runtime.minIntegerValue 
            else -runtime.maxIntegerValue;
    
    value length = string.size;
    variable value result = 0;
    variable value digitIndex = 0;
    variable value index = start;
    while (index < length) {
        assert (exists ch = string.getFromFirst(index));

        if (index + 1 == length && 
                radix == 10 && 
                ch in "kMGTP") {
            // The SI-style magnitude
            "unrecognized SI magnitude"
            assert (exists exp = parseIntegerExponent(ch));
            Integer magnitude = 10^exp;
            if ((limit / magnitude) < result) {
                result *= magnitude;
                break;
            }
            else {
                // overflow
                return ParseException("numeric value is too large for Integer");
            }
        }
        else if (exists digit = parseDigit(ch, radix)) {
            // A regular digit
            if (result < max) { 
                // overflow
                return ParseException("numeric value too large for Integer");
            }
            result *= radix;
            if (result < limit + digit) { 
                // overflow
                return ParseException("numeric value too large for Integer");
            }
            // += would be much more obvious, but it doesn't work for minIntegerValue
            result -= digit;
        }
        else {
            // Invalid character
            return ParseException("illegal format for Integer: unexpected character '``ch``'");
        }
        
        index++;
        digitIndex++;
    }
    
    if (digitIndex == 0) {
        return ParseException("illegal format for Integer: no digits");
    }
    else {
        return negative then result else -result;
    }
}

Integer? parseIntegerExponent(Character char) {
    switch (char)
    case ('P') {
        return 15;
    } 
    case ('T') {
        return 12; 
    } 
    case ('G') {
        return 9; 
    } 
    case ('M') {
        return 6;
    } 
    case ('k') {
        return 3;
    }
    else {
        return null;
    }
}

Integer aIntLower = 'a'.integer;
Integer aIntUpper = 'A'.integer;
Integer zeroInt = '0'.integer;

Integer? parseDigit(Character digit, Integer radix) {
    Integer figure;
    Integer digitInt = digit.integer;
    if (0<=digitInt-zeroInt<10) {
        figure=digitInt-zeroInt;
    }
    else if (0<=digitInt-aIntLower<26) {
        figure=digitInt-aIntLower+10;
    }
    else if (0<=digitInt-aIntUpper<26) {
        figure=digitInt-aIntUpper+10;
    }
    else {
        return null;
    }
    return figure<radix then figure;
}
