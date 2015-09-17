"The [[Float]] value of the given 
 [[string representation|string]] of a decimal floating 
 point number, or `null` if the string does not represent a 
 decimal floating point number.
 
 The syntax accepted by this method is the same as the 
 syntax for a `Float` literal in the Ceylon language 
 except that it may optionally begin with a sign 
 character (`+` or `-`) and may not contain grouping 
 underscore characters."
see (`function parseInteger`)
tagged("Numbers")
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
    
    if (exists whole = parseInteger(wholePart), 
        exists fractional = parseInteger(fractionalPart)) {
        value shift = fractionalPart.size;
        Integer exponent;
        if (exists rest) {
            if (exists magnitude
                = parseFloatExponent(rest)) {
                exponent = magnitude-shift;
            }
            else {
                return null;
            }
        }
        else {
            exponent = -shift; 
        }
        Integer numerator = whole*10^shift + fractional;
        Float signedNumerator 
                = numerator.zero
                      then 0 * sign.float //preserve sign of -0.0
                      else (sign * numerator).float;
        value exponentMagnitude = exponent.magnitude;
        if (exponentMagnitude==0) {
            return signedNumerator;
        }
        else if (exponentMagnitude<maximumIntegerExponent) {
            value scale = 10^exponentMagnitude;
            return exponent<0
                then signedNumerator / scale
                else signedNumerator * scale;
        }
        else {
            //scale can't be represented as 
            //an integer, resulting in some
            //rounding error
            return signedNumerator * 10.0^exponent;
        }
    }
    
    return null;
}

//TODO: replace with a native implementation
Integer maximumIntegerExponent 
        = smallest(runtime.maxIntegerValue.string.size,
                   runtime.minIntegerValue.string.size-1);

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