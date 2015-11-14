
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
    
    // parse the sign first
    Integer sign;
    String unsignedPart;
    if (string.startsWith("-")) {
        sign = -1;
        unsignedPart = string[1...];
    }
    else if (string.startsWith("+")) {
        sign = +1;
        unsignedPart = string[1...];
    }
    else {
        sign = +1;
        unsignedPart = string;
    }
    // split into three main parts
    String wholePart;
    String fractionalPart;
    String? rest;
    if (exists dot = unsignedPart.firstOccurrence('.')) {
        wholePart = unsignedPart[...dot-1];
        String afterWholePart = unsignedPart[dot+1...];
        if (exists mag 
            = afterWholePart.firstIndexWhere(Character.letter)) {
            fractionalPart = afterWholePart[...mag-1];
            rest = afterWholePart[mag...];
        }
        else {
            fractionalPart = afterWholePart;
            rest = null;
        }
    }
    else {
        if (exists mag
            = unsignedPart.firstIndexWhere(Character.letter)) {
            wholePart = unsignedPart[...mag-1];
            rest = unsignedPart[mag...];
        }
        else {
            wholePart = unsignedPart;
            rest = null;
        }
        fractionalPart = "0";
    }
    
    if (!wholePart.every(Character.digit) ||
        !fractionalPart.every(Character.digit)) {
        return null;
    }
    
    value usableWholePart 
            = wholePart[0:maximumIntegerExponent];
    value usableFractionalPart 
            = fractionalPart[0:
                maximumIntegerExponent
                    - usableWholePart.size];
    
    value digits = usableWholePart + usableFractionalPart;
    value shift 
            = usableFractionalPart.empty
            then usableWholePart.size - wholePart.size
            else usableFractionalPart.size;
    
    Integer exponent;
    if (exists rest) {
        if (exists magnitude
                = parseFloatExponent(rest)) {
            exponent = magnitude - shift;
        }
        else {
            return null;
        }
    }
    else {
        exponent = -shift; 
    }
    
    if (exists unsigned = parseInteger(digits)) {
        Float signed
                = unsigned == 0
                then 0 * sign.float //preserve sign of -0.0
                else (sign * unsigned).nearestFloat;
        value exponentMagnitude = exponent.magnitude;
        if (exponentMagnitude == 0) {
            return signed;
        }
        else if (exponentMagnitude<=maximumIntegerExponent) {
            value scale = 10^exponentMagnitude;
            return exponent<0
            then signed / scale
            else signed * scale;
        }
        else {
            //scale can't be represented as 
            //an integer, resulting in some
            //rounding error
            return signed * 10.0^exponent;
        }
    }
    
    return null;
}

//TODO: replace with a native implementation
"The maximum number of decimal digits that can be 
 represented by an [[Integer]]."
Integer maximumIntegerExponent
        = smallest(runtime.maxIntegerValue.string.size,
                   runtime.minIntegerValue.string.size-1)
            - 1;

Integer? parseFloatExponent(String string) {
    switch (string)
    case ("k") {
        return 3;
    }
    case ("M") {
        return 6;
    }
    case ("G") {
        return 9;
    }
    case ("T") {
        return 12;
    }
    case ("P") {
        return 15;
    }
    case ("m") {
        return -3;
    }
    case ("u") {
        return -6;
    }
    case ("n") {
        return -9;
    }
    case ("p") {
        return -12;
    }
    case ("f") {
        return -15;
    }
    else {
        if (string.lowercased.startsWith("e") &&
            string.rest.every(digitOrSign)) {
            return parseInteger(string.rest);
        }
        else {
            return null;
        }
    }
}

Boolean(Character) digitOrSign 
        = or(Character.digit, "+-".contains);