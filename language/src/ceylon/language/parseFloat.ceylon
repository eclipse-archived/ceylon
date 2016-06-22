import java.lang {
    JDouble=Double
}

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
 underscore characters."
see (`function formatFloat`, 
     `function parseInteger`)
tagged("Numbers", "Basic types")
shared Float? parseFloat(String string) {
    // ("-"|"+")?
    // Digit+ | ("." Digit+) | (Digit+ "." Digit*)
    // (("E"|"e") ("+"|"-")? Digit+)
    //      | Magnitude | FractionalMagnitude

    variable Boolean first = true;
    variable Boolean seenDigit = false;
    variable Boolean seenDecimal = false;
    variable Boolean inDigitPart = true;
    variable Boolean inExponentPart = false;
    variable Boolean inSuffixPart = false;
    variable Integer digitCount = 0;
    variable Boolean seenExponentDigit = false;
    variable Integer suffixExponent = 0;

    for (c in string) {
        if (inDigitPart) {
            digitCount++;
            if (first) {
                first = false;
                if (c == '-' || c == '+') {
                    continue;
                }
            }
            if (c == '.') {
                if (seenDecimal) {
                    return null;
                }
                seenDecimal = true;
                continue;
            }
            if ('0' <= c <= '9') {
                seenDigit = true;
                continue;
            }
            digitCount--;
        }
        inDigitPart = false;

        if (inSuffixPart) {
            // illegal extra character
            return null;
        }

        if (inExponentPart) {
            if (first) {
                first = false;
                if (c == '-' || c == '+') {
                    continue;
                }
            }
            if ('0' <= c <= '9') {
                seenExponentDigit = true;
                continue;
            }
            return null;
        }

        if (c == 'e' || c == 'E') {
            inExponentPart = true;
            first = true;
            continue;
        }

        if (c in "PTGMkmunpf") {
            suffixExponent = parseSuffix(c);
            inSuffixPart = true;
            continue;
        }

        return null;
    }

    if (!seenDigit) {
        // '.1' is ok, but not just '.' or 'e10'
        return null;
    }

    if (inExponentPart && !seenExponentDigit) {
        return null;
    }

    if (suffixExponent != 0) {
        // Ceylon style magnitude suffix
        return nativeParseFloat(
            "``string[0:digitCount]``\
             E``suffixExponent``");
    }
    else {
        // may or may not have exponent
        return nativeParseFloat(string);
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
        assert (false);
    }
}

native
Float? nativeParseFloat(String string);

native("jvm")
Float? nativeParseFloat(String string)
    =>  JDouble.parseDouble(string);

native("js")
Float? nativeParseFloat(String string) {
    Float result;
    dynamic {
        result = Float(eval("parseFloat(\"``string``\")"));
    }
    if (result == 0.0 && string.occursAt(0, '-')) {
        return -0.0;
    }
    return result; 
}
