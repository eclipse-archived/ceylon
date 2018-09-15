/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"The [[Float]] value of the given 
 [[string representation|string]] of a decimal floating 
 point number, or `null` if the string does not represent a 
 decimal floating point number.
 
 If the given string representation contains more digits
 than can be represented by a `Float`, then the least 
 significant digits are ignored.
 
 The syntax accepted by this method is the same as the 
 syntax for a `Float` literal in the Ceylon language 
 except that it may optionally begin with a sign 
 character (`+` or `-`) and may not contain grouping 
 underscore characters. That is, an optional sign character,
 followed by a string of decimal digits, followed by an
 optional decimal point and string of decimal digits, 
 followed by an optional decimal exponent, for example 
 `e+10` or `E-5`, or SI magnitude, `k`, `M`, `G`, `T`, `P`, 
 `m`, `u`, `n`, `p`, or `f`.
 
     Float: Sign? Digits ('.' Digits)? (Magnitude|Exponent)
     Sign: '+' | '-'
     Magnitude: 'k' | 'M' | 'G' | 'T' | 'P' | 'm' | 'u' | 'n' | 'p' | 'f'
     Exponent: ('e'|'E') Sign? Digits
     Digits: ('0'..'9')+"
see (function Float.parse)
tagged("Numbers", "Basic types")
shared Float? parseFloat(String? string)
        => if (exists string,
               is Float result 
                    = parseFloatInternal(string))
        then result
        else null;

final class ParseFloatState 
        of start 
         | afterPlusMinus 
         | digitsBeforeDecimal 
         | afterJustDecimal 
         | afterDecimal 
         | digitsAfterDecimal 
         | afterE 
         | exponentDigits 
         | afterEPlusMinus 
         | afterSuffix 
         | invalid {
    
    shared new start {}
    shared new afterPlusMinus {}
    shared new digitsBeforeDecimal {}
    shared new afterJustDecimal {}
    shared new afterDecimal {}
    shared new digitsAfterDecimal {}
    shared new afterE {}
    shared new exponentDigits {}
    shared new afterEPlusMinus {}
    shared new afterSuffix {}
    shared new invalid {}
}

Float|ParseException parseFloatInternal(String string) {

    import ceylon.language {
        ParseFloatState {
            start, afterPlusMinus, digitsBeforeDecimal,
            afterJustDecimal, afterDecimal, digitsAfterDecimal,
            afterE, exponentDigits, afterEPlusMinus,
            afterSuffix, invalid
        }
    }
    
    // ("-"|"+")?
    // (Digit* "." Digit+) | (Digit+ "."?)
    // (("E"|"e") ("+"|"-")? Digit+) | suffix

    variable value state = start;
    variable value size = 0;
    variable Integer? suffixExponent = null;

    for (ch in string) {
        size++;
        state = switch (state)
        case (start)
            if (ch == '+' || ch == '-')
                then afterPlusMinus
            else if ('0' <= ch <= '9')
                then digitsBeforeDecimal
            else if (ch == '.')
                then afterJustDecimal
            else invalid
        case (afterPlusMinus)
            if ('0' <= ch <= '9')
                then digitsBeforeDecimal
            else if (ch == '.')
                then afterJustDecimal
            else invalid
        case (digitsBeforeDecimal)
            if ('0' <= ch <= '9')
                then digitsBeforeDecimal
            else if (ch == '.')
                then afterDecimal
            else if (ch == 'e' || ch == 'E')
                then afterE
            else if (ch in "PTGMkmunpf")
                then afterSuffix
            else invalid
        case (afterJustDecimal)
            if ('0' <= ch <= '9')
                then digitsAfterDecimal
            else invalid
        case (digitsAfterDecimal |
              afterDecimal)
            if ('0' <= ch <= '9')
                then digitsAfterDecimal
            else if (ch == 'e' || ch == 'E')
                then afterE
            else if (ch in "PTGMkmunpf")
                then afterSuffix
            else invalid
        case (afterE)
            if ('0' <= ch <= '9')
                then exponentDigits
            else if (ch == '+' || ch == '-')
                then afterEPlusMinus
            else invalid
        case (exponentDigits |
              afterEPlusMinus)
            if ('0' <= ch <= '9')
                then exponentDigits
            else invalid
        case (afterSuffix)
            invalid
        case (invalid)
            invalid;

        if (state == afterSuffix) {
            suffixExponent = parseSuffix(ch);
        }

        if (state == invalid) {
            return ParseException("illegal format for Float: unexpected character '``ch``'");
        }
    }

    if (!state in [digitsBeforeDecimal,
            afterDecimal, digitsAfterDecimal,
            exponentDigits, afterSuffix]) {
        return ParseException("illegal format for Float: unexpected end of string");
    }

    try {
        if (exists exponent = suffixExponent) {
            // Ceylon style magnitude suffix
            return nativeParseFloat(string[0:size-1] + "E" + exponent.string);
        }
        else {
            // may or may not have exponent
            return nativeParseFloat(string);
        }
    }
    catch (e) {
        return ParseException("illegal format for Float: " + e.message);
    }
}

Integer parseSuffix(Character suffix) {
    switch (suffix)
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
    case ('m') {
        return -3;
    }
    case ('u') {
        return -6;
    }
    case ('n') {
        return -9;
    }
    case ('p') {
        return -12;
    }
    case ('f') {
        return -15;
    }
    else {
        "unrecognized SI magnitude"
        assert (false);
    }
}

native
Float nativeParseFloat(String string);

native("jvm")
Float nativeParseFloat(String string) {
    import java.lang {
        Double {
            parseDouble
        }
    }

    return parseDouble(string);
}

native("js")
Float nativeParseFloat(String string) {
    Float result;
    dynamic {
        result = nativeJSParseFloat(string);
    }
    if (result == 0.0 && string.occursAt(0, '-')) {
        return -0.0;
    }
    return result; 
}
