"The [[Float]] value of the given 
 [[string representation|string]] of a decimal floating 
 point number, or `null` if the string does not represent a 
 decimal floating point number.
 
 The syntax accepted by this method is the same as the 
 syntax for a `Float` literal in the Ceylon language 
 except that it may optionally begin with a sign 
 character (`+` or `-`)."
see (`function parseInteger`)
shared Float? parseFloat(String string) {
    value dot = string.firstOccurrence('.');
    if (exists dot) {
        //TODO: handle shortcut format, e.g. 12u
        value wholePart = string[...dot-1];
        value positiveWholePart =
                wholePart.startsWith("+") ||
                wholePart.startsWith("-") 
        then wholePart.rest else wholePart;
        value nonDigit = 
                not(or(Character.digit, '_'.equals));
        if (positiveWholePart any nonDigit) {
            return null;
        }
        value afterWholePart = string[dot+1...];
        value mag = afterWholePart firstIndexWhere nonDigit;
        String fractionalPart;
        String? rest;
        if (exists mag) {
            fractionalPart = afterWholePart[...mag-1];
            rest = afterWholePart[mag...];
        }
        else {
            fractionalPart = afterWholePart;
            rest = null;
        }
        value whole = parseInteger(wholePart);
        value fractional = parseInteger(fractionalPart);
        if (exists whole, exists fractional) {
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
            value em = exponent.magnitude;
            if (em==0) {
                return numerator.float;
            }
            else if (em<maximumIntegerExponent) {
                value scale = 10^em;
                if (exponent<0) {
                    return numerator.float / scale;
                }
                else {
                    return numerator.float * scale;
                }
            }
            else {
                //scale can't be represented as 
                //an integer, resulting in some
                //rounding error
                return numerator * 10.0^exponent;
            }
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
        if (string.lowercased.startsWith("e")) {
            return parseInteger(string.rest);
        }
    }
    return null;
}