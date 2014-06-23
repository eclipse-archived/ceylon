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
 value is `10`."
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
    
    assert (minRadix <= radix <= maxRadix); 
    
    variable Integer index = 0;
    Integer max = runtime.minIntegerValue / radix;
    
    // Parse the sign
    Boolean negative;
    if (exists char = string[index]) {
        if (char == '-') {
            negative = true;
            index++;
        }
        else if (char == '+') {
            negative = false;
            index++;
        }
        else {
            negative = false;
        }
    }
    else {
        return null;
    }
    
    Integer limit = negative 
            then runtime.minIntegerValue 
            else -runtime.maxIntegerValue;
    
    Integer length = string.size;
    variable Integer result = 0;
    variable Integer digitIndex = 0;
    while (index < length) {
        Character ch;
        if (exists char = string[index]) {
            ch = char;
        }
        else {
            return null;
        }
        
        if (index + 1 == length && 
                radix == 10 && 
                ch in "kMGTP") {
            // The SI-style magnitude
            if (exists exp = parseIntegerExponent(ch)) {
                Integer magnitude = 10^exp;
                if ((limit / magnitude) < result) {
                    result *= magnitude;
                    break;
                }
                else {
                    // overflow
                    return null;
                }
            }
            else {
                return null;
            }
        }
        else if (exists digit = parseDigit(ch, radix)) {
            // A regular digit
            if (result < max) { 
                // overflow
                return null;
            }
            result *= radix;
            if (result < limit + digit) { 
                // overflow
                return null;
            }
            // += would be much more obvious, but it doesn't work for minIntegerValue
            result -= digit;
        }
        else {
            // Invalid character
            return null;
        }
        
        index++;
        digitIndex++;
    }
    
    if (digitIndex == 0) {
        return null;
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
