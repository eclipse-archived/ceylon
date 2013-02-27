doc "The `Integer` value of the given string representation 
     of an integer, or `null` if the string does not represent 
     an integer or if the mathematical integer it represents 
     is too large in magnitude to be represented by an 
     `Integer`.
     
     The syntax accepted by this method is the same as the 
     syntax for an `Integer` literal in the Ceylon language 
     except that it may optionally begin with a sign 
     character (`+` or `-`)."
shared Integer? parseInteger(String string, Integer radix = 10) {
    Integer length = string.size;
    if (length == 0) {
        return null;
    }
    variable Integer ii = 0;
    variable Character ch;
    if (exists char = string[ii]) {
        ch = char;
    } else {
        return null;
    }
    variable Integer result = 0;
    Integer max = minIntegerValue / radix;
    Boolean negative;
    if (ch == '-') {
        negative = true;
        ii++;
    } else if (ch == '+') {
        negative = false;
        ii++;
    } else {
        negative = false;
    }
    Integer limit = negative then minIntegerValue else -maxIntegerValue;
    
    // The actual number
    variable Integer sep = -1;
    variable Integer digitIndex = 0;
    while (ii < length) {
        if (exists char = string[ii]) {
            ch = char;
        } else {
            return null;
        }
        if (ch == '_') {
            if (sep != -1 && (digitIndex - sep) % 4 != 0) {
                return null;
            }
            if (sep == -1) {
                // at most three digits before the first _
                if (digitIndex > 3) {
                    return null;
                }
                sep = digitIndex;
            }
        }
        else {
            if (sep != -1 && (digitIndex - sep) % 4 == 0) {
                // missing a _ after the first
                return null;
            }
            if (ii + 1 == length && radix == 10 && ch in ['k','M','G','T','P']) {
                // base 10 factor
                break;
            }
            Integer digit;
            if (exists d = parseDigit(ch, radix)) {
                digit = d;
            } else { // Invalid digit
                return null;
            }
            if (result < max) { // overflow
                return null;
            }
            result *= radix;
            if (result < limit + digit) { // overflow
                return null;
            }
            // += would be much more obvious, but it doesn't work for minIntegerValue
            result -= digit;
        }
        ii++;
        digitIndex++;
    }
    // check for insufficient digits after the last _
    if (sep != -1 && ((digitIndex - sep) % 4) != 0) {
        return null;
    }
    // The magnitude
    variable Integer magnitude;
    if (negative) {
        magnitude = 1;
    } else {
        magnitude= -1;
    }
    if (ii < length) {
        if (radix != 10) {
            return null;
        }
        Integer power;
        if (exists char = string[ii++]) {
            if (char == 'P') {
                power = 15;
            }
            else if (char == 'T') {
                power = 12; 
            }
            else if (char == 'G') {
                power = 9; 
            }
            else if (char == 'M') {
                power = 6;
            }
            else if (char == 'k') {
                power = 3;
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
        magnitude *= radix ^ power;
    }
    if (ii < length || digitIndex == 0) {
        return null;
    }
    return result * magnitude;
}

Map<Character,Integer> digitCharacters = LazyMap {
    '0' -> 0,
    '1' -> 1,
    '2' -> 2,
    '3' -> 3,
    '4' -> 4,
    '5' -> 5,
    '6' -> 6,
    '7' -> 7,
    '8' -> 8,
    '9' -> 9,
    'a' -> 10,
    'b' -> 11,
    'c' -> 12,
    'd' -> 13,
    'e' -> 14,
    'f' -> 15,
    'g' -> 16,
    'h' -> 17,
    'i' -> 18,
    'j' -> 19,
    'k' -> 20,
    'l' -> 21,
    'm' -> 22,
    'n' -> 23,
    'o' -> 24,
    'p' -> 25,
    'q' -> 26,
    'r' -> 27,
    's' -> 28,
    't' -> 29,
    'u' -> 30,
    'v' -> 31,
    'w' -> 32,
    'x' -> 33,
    'y' -> 34,
    'z' -> 35
};

Integer? parseDigit(Character digit, Integer radix) {
    Integer? figure = digitCharacters.get(digit.lowercased);
    if (exists figure, figure < radix) {
        return figure;
    }
    return null;
}