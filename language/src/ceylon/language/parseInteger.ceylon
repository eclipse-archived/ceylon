Integer minRadix = 2;
Integer maxRadix = 36;

"The `Integer` value of the given string representation 
 of an integer, or `null` if the string does not represent 
 an integer or if the mathematical integer it represents 
 is too large in magnitude to be represented by an 
 `Integer`.
 
 The syntax accepted by this function is the same as the 
 syntax for an `Integer` literal in the Ceylon language 
 except that it may optionally begin with a sign 
 character (`+` or `-`).
 
 A radix can be given in input to specify what is the base
 to take in consideration for the parsing. radix has to be
 between `minRadix` and `maxRadix` included.
 The list of available digits starts from `0` to `9` followed
 by `a` to `z`.
 When parsing in a specific base, the first `radix` digits
 from the available digits list can be used.
 This function is not case sensitive; `a` and `A` both
 correspond to the `a` digit which decimal value is `10`.
  
 `_` character can be used to separate groups of digits
 for bases 2, 10 and 16 as for `Integer` literal in the
 Ceylon language. For any other bases, no grouping is
 supported."
throws (`class AssertionException`, 
        "if `radix` is not between `minRadix` and `maxRadix`")
shared Integer? parseInteger(String string, Integer radix = 10) {
    assert (radix >= minRadix, radix <= maxRadix); 
    variable Integer ii = 0;
    Integer max = machine.minIntegerValue / radix;
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
    Integer limit = negative then machine.minIntegerValue else -machine.maxIntegerValue;
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
                    ch in ['k','M','G','T','P']) {
                // The magnitude
                if (exists magnitude = computeMagnitude(radix, string[ii++])) {
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

Integer? computeMagnitude(Integer radix, Character? char) {
    Integer? power;
    if (exists char) {
        if (char == 'P') {
            power = 15;
        } else if (char == 'T') {
            power = 12; 
        } else if (char == 'G') {
            power = 9; 
        } else if (char == 'M') {
            power = 6;
        } else if (char == 'k') {
            power = 3;
        } else {
            power = null;
        }
    } else {
        power = null;
    }
    if (exists power) {
        return radix^power;
    }
    return null;
}

Integer aInt = 'a'.integer;
Integer zeroInt = '0'.integer;

Integer? parseDigit(Character digit, Integer radix) {
    Integer figure;
    Integer digitInt = digit.integer;
    if (0<=digitInt-zeroInt<10) {
        figure=digitInt-zeroInt;
    }
    else if (0<=digitInt-aInt<26) {
        figure=digitInt-aInt+10;
    }
    else {
        return null;
    }
    return figure<radix then figure;
}

"The string representation of `integer` in the `radix` base.
 `radix` must be between `minRadix` and `maxRadix` included.
 
 If `integer` is negative, returned string will start by character `-`"
throws (`class AssertionException`, 
        "if `radix` is not between `minRadix` and `maxRadix`")
shared String formatInteger(Integer integer, Integer radix = 10) {
    assert (radix >= minRadix, radix <= maxRadix);
    if (integer == 0) {
        return "0";
    }
    StringBuilder digits = StringBuilder();
    Integer insertIndex;
    variable Integer i;
    if (integer < 0) {
        digits.append("-");
        insertIndex = 1;
        i = integer;
    } else {
        insertIndex = 0;
        i = -integer;
    }
    while (i != 0) {
        Integer d = -(i % radix);
        Character c;
        if (0<=d<10) {
            c = (d+zeroInt).character;
        }
        else if (10<=d<36) {
            c = (d-10+aInt).character;
        }
        else {
            assert (false);
        }
        digits.insertCharacter(insertIndex, c);
        i = (i + d) / radix;
    }
    return digits.string;
}
