import java.lang {
    JDouble=Double
}
import ceylon.language {
    ParseFloatState {
        start, afterPlusMinus, digitsBeforeDecimal,
        afterJustDecimal, afterDecimal, digitsAfterDecimal,
        afterE, exponentDigits, afterEPlusMinus,
        afterSuffix, invalid
    }
}

class ParseFloatState of
        start | afterPlusMinus | digitsBeforeDecimal |
        afterJustDecimal | afterDecimal |
        digitsAfterDecimal | afterE | exponentDigits |
        afterEPlusMinus | afterSuffix | invalid {

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
    // (Digit* "." Digit+) | (Digit+ "."?)
    // (("E"|"e") ("+"|"-")? Digit+) | suffix

    variable value state = start;
    variable value size = 0;
    variable Integer? suffixExponent = null;

    for (c in string) {
        size++;
        state = switch (state)
        case (start)
            if (c == '+' || c == '-')
                then afterPlusMinus
            else if ('0' <= c <= '9')
                then digitsBeforeDecimal
            else if (c == '.')
                then afterJustDecimal
            else invalid
        case (afterPlusMinus)
            if ('0' <= c <= '9')
                then digitsBeforeDecimal
            else if (c == '.')
                then afterJustDecimal
            else invalid
        case (digitsBeforeDecimal)
            if ('0' <= c <= '9')
                then digitsBeforeDecimal
            else if (c == '.')
                then afterDecimal
            else if (c == 'e' || c == 'E')
                then afterE
            else if (c in "PTGMkmunpf")
                then afterSuffix
            else invalid
        case (afterJustDecimal)
            if ('0' <= c <= '9')
                then digitsAfterDecimal
            else invalid
        case (digitsAfterDecimal |
              afterDecimal)
            if ('0' <= c <= '9')
                then digitsAfterDecimal
            else if (c == 'e' || c == 'E')
                then afterE
            else if (c in "PTGMkmunpf")
                then afterSuffix
            else invalid
        case (afterE)
            if ('0' <= c <= '9')
                then exponentDigits
            else if (c == '+' || c == '-')
                then afterEPlusMinus
            else invalid
        case (exponentDigits |
              afterEPlusMinus)
            if ('0' <= c <= '9')
                then exponentDigits
            else invalid
        case (afterSuffix)
            invalid
        case (invalid)
            invalid;

        if (state == afterSuffix) {
            suffixExponent = parseSuffix(c);
        }

        if (state == invalid) {
            return null;
        }
    }

    if (!state in [digitsBeforeDecimal,
            afterDecimal, digitsAfterDecimal,
            exponentDigits, afterSuffix]) {
        return null;
    }

    if (exists exponent = suffixExponent) {
        // Ceylon style magnitude suffix
        return nativeParseFloat(
            "``string[...size-2]``\
             E``exponent``");
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
