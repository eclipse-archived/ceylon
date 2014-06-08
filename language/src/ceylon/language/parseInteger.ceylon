Integer minRadix = 2;
Integer maxRadix = 36;

"The [[Integer]] value of the given 
 [[string representation|string]] of an integer value in the 
 base given by [[radix]], or `null` if the string does not 
 represent an integer in that base, or if the mathematical 
 integer it represents is too large in magnitude to be 
 represented by an instance of the class `Integer`.
 
 The syntax accepted by this function is the same as the 
 syntax for an `Integer` literal in the Ceylon language 
 except that it may optionally begin with a sign 
 character (`+` or `-`).
 
 The given `radix` specifies the base of the string 
 representation. The list of available digits starts from 
 `0` to `9`, followed by `a` to `z`. When parsing in a 
 specific base, the first `radix` digits from the available 
 digits list is used. This function is not case sensitive; 
 `a` and `A` both correspond to the digit `a` whose decimal 
 value is `10`.
 
 The `_` character may be used to separate groups of digits
 for bases 2, 10, and 16, as for `Integer` literals in the
 Ceylon language. For any other base, no grouping is
 supported."
throws (`class AssertionError`, 
        "if [[radix]] is not between [[minRadix]] and 
         [[maxRadix]]")
see (`function formatInteger`,
     `function parseFloat`)
shared Integer? parseInteger(
            "The string representation to parse."
            String string,
            "The base, between [[minRadix]] and [[maxRadix]] 
             inclusive."
            Integer radix = 10) {
    assert (radix >= minRadix, radix <= maxRadix); 
    variable Integer ii = 0;
    Integer max = runtime.minIntegerValue / radix;
    Boolean negative;
    if (exists char = string[ii]) {
        if (char == '-') {
            negative = true;
            ii++;
        } else if (char == '+') {
            negative = false;
            ii++;
        } else {
            negative = false;
        }
    } else {
        return null;
    }
    Integer limit = negative 
            then runtime.minIntegerValue 
            else -runtime.maxIntegerValue;
    Integer length = string.size;
    variable Integer result = 0;
    variable Integer sep = -1;
    variable Integer digitIndex = 0;
    variable Integer groupingSize = -1;
    while (ii < length) {
        Character ch;
        if (exists char = string[ii]) {
            ch = char;
        } else {
            return null;
        }
        if (ch == '_') {
            if (sep == -1) {
                if (exists digitGroupSize = 
                        computeDigitGroupingSize(radix, digitIndex, string, ii), 
                        digitIndex <= digitGroupSize) {
                    groupingSize = digitGroupSize;
                    sep = digitIndex;
                } else {
                    return null;
                }
            } else if ((digitIndex - sep) == groupingSize) {
                return null;
            } else {
                sep = digitIndex;
            }
        } else {
            if (sep != -1 && 
                    (digitIndex - sep) == (groupingSize + 1)) {
                return null;
            }
            if (ii + 1 == length && 
                    radix == 10 && 
                    ch in "kMGTP") {
                // The magnitude
                if (exists exp = parseIntegerExponent(ch)) {
                    Integer magnitude = 10^exp;
                    if ((limit / magnitude) < result) {
                        result *= magnitude;
                        break;
                    } else { // overflow
                        return null;
                    }
                } else {
                    return null;
                }
            } else if (exists digit = parseDigit(ch, radix)) {
                if (result < max) { // overflow
                    return null;
                }
                result *= radix;
                if (result < limit + digit) { // overflow
                    return null;
                }
                // += would be much more obvious, but it doesn't work for minIntegerValue
                result -= digit;
            } else { // Invalid digit
                return null;
            }
        }
        ii++;
        digitIndex++;
    }
    // check for insufficient digits after the last _
    if (sep != -1 && 
            (digitIndex - sep) != (groupingSize + 1)) {
        return null;
    }
    if (digitIndex == 0) {
        return null;
    }
    return negative then result else -result;
}

Integer? computeDigitGroupingSize(Integer radix, 
        Integer digitIndex, String string, Integer ii) {
    Integer? groupingSize;
    if (radix == 2) {
        groupingSize = 4;
    } else if (radix == 10) {
        groupingSize = 3;
    } else if (radix == 16) {
        if (digitIndex <= 2, 
                exists char = string[ii + 3], 
                char == '_') {
            groupingSize = 2;
        } else {
            groupingSize = 4;
        }
    } else {
        groupingSize = null;
    }
    return groupingSize;
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
