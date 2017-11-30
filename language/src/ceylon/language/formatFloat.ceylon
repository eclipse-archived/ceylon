/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"The string decimal representation of the given 
 [[floating point number|float]]. If the given number is 
 [[negative|Float.negative]], the string representation will 
 begin with `-`. The [[whole part|Float.wholePart]] and 
 [[fractional parts|Float.fractionalPart]] of the number are
 separated by a `.` decimal point. Digits consist of decimal 
 digits `0` to `9`. 
 
 The number of decimal places following the decimal point is 
 controlled by the parameters [[minDecimalPlaces]] and 
 [[maxDecimalPlaces]], which default to `1` and `9` 
 respectively, so that by default the string representation
 always contains a decimal point, and never contains more 
 than nine decimal places. The decimal representation is 
 rounded so that the number of decimal places never
 exceeds the specified maximum.
 
 For example:
 
 - `formatFloat(1234.1234)` is `\"1234.1234\"`
 - `formatFloat(0.1234)` is `\"0.1234\"`
 - `formatFloat(1234.0)` is `\"1234.0\"`
 - `formatFloat(1234.0,0)` is `\"1234\"`
 - `formatFloat(1234.1234,6)` is `\"1234.123400\"`
 - `formatFloat(1234.1234,0,2)` is `\"1234.12\"`
 - `formatFloat(1234.123456,0,5)` is `\"1234.12346\"`
 - `formatFloat(0.0001,2,2)` is `\"0.00\"`
 - `formatFloat(0.0001,0,2)` is `\"0\"`
 
 Finally:
 
 - `formatFloat(-0.0)` is `\"0.0\"`,
 - `formatFloat(0.0/0)` is `\"NaN\"`,
 - `formatFloat(1.0/0)` is `\"Infinity\"`, and
 - `formatFloat(-1.0/0)` is `\"-Infinity\".`
 
 This function never produces a representation involving 
 scientific notation."
tagged("Numbers")
see (function Float.format)
since("1.2.0")
deprecated("Use [[Float.format]]")
shared String formatFloat(
        "The floating point value to format."
        Float float,
        "The minimum number of allowed decimal places.

         If `minDecimalPlaces<=0`, the result may have no
         decimal point."
        variable Integer minDecimalPlaces=1,
        "The maximum number of allowed decimal places.

         If `maxDecimalPlaces<=0`, the result always has no
         decimal point."
        variable Integer maxDecimalPlaces=9,
        "The character to use as the decimal separator.

         `decimalSeparator` may not be '-' or a digit as
         defined by the Unicode general category *Nd*."
        Character decimalSeparator = '.',
        "If not `null`, `thousandsSeparator` will be used to
         separate each group of three digits, starting
         immediately to the left of the decimal separator.

         `thousandsSeparator` may not be equal to the
         decimalSeparator and may not be '-' or a digit as
         defined by the Unicode general category *Nd*."
        Character? thousandsSeparator = null) {

    if (exists thousandsSeparator) {
        "thousandsSeparator may not be '-' or a numeric digit."
        assert (!thousandsSeparator.digit
                && !thousandsSeparator == '-');

        "The same character may not be used for both
         thousandsSeparator and decimalSeparator."
        assert (thousandsSeparator != decimalSeparator);
    }

    "The decimalSeparator may not be '-' or a numeric digit."
    assert (!decimalSeparator.digit
            && !decimalSeparator == '-');

    // let's not be rude and throw
    if (maxDecimalPlaces < 0) {
        maxDecimalPlaces = 0;
    }
    if (minDecimalPlaces < 0) {
        minDecimalPlaces = 0;
    }
    if (maxDecimalPlaces < minDecimalPlaces) {
        maxDecimalPlaces = minDecimalPlaces;
    }

    // handle 0, undefined, and infinities
    if (float == 0) {
        if (minDecimalPlaces > 0) {
            return "0``decimalSeparator````"0".repeat(minDecimalPlaces)``";
        }
        return "0";
    }
    else if (float.undefined || float.infinite) {
        return float.string;
    }

    variable value wholeDigitNumber = 0;
    value thousands = thousandsSeparator?.string else "";

    value result = StringBuilder();
    value magnitude = float.magnitude;
    value decimalMoveRight = smallest {
        // Don't include more fractional digits than
        // necessary. See rounding (halfEven) below.
        maxDecimalPlaces;
        14 - exponent(magnitude);
    };

    "The float, but with all meaningful digits shifted to the
     first ~15 positions of the whole part"
    value normalized = scaleByPowerOfTen(magnitude, decimalMoveRight);

    "The usable digits: [[normalized]] as an [[Integer]] after rounding"
    variable value integer = halfEven(normalized).integer;

    "The number of digits of [[integer]] that are to the right of
     the decimal point in [[float]]. May be negative."
    variable value fractionalPartDigits = decimalMoveRight;

    "Have any digits to the right of the '.' been emitted?"
    variable value emittedFractional = false;

    if (minDecimalPlaces > fractionalPartDigits) {
        // we have fewer fractional digits than we need
        value emitZeros = if (fractionalPartDigits > 0) 
            then minDecimalPlaces - fractionalPartDigits
            else minDecimalPlaces;
        result.append("0".repeat(emitZeros));
        emittedFractional = emitZeros > 0;
    }
    while (fractionalPartDigits > maxDecimalPlaces) {
        // we have more fractional digits than we need
        integer /= 10;
        fractionalPartDigits--;
    }
    while (fractionalPartDigits > 0) {
        // emit fractional part of 'integer'
        value digit = integer % 10;
        integer /= 10;
        if (digit != 0 || emittedFractional
                || fractionalPartDigits <= minDecimalPlaces) {
            result.appendCharacter('0'.neighbour(digit));
            emittedFractional = true;
        }
        fractionalPartDigits--;
    }
    if (emittedFractional) {
        result.appendCharacter(decimalSeparator);
    }
    if (integer == 0) {
        result.appendCharacter('0');
    }
    else {
        while (fractionalPartDigits++ < 0) {
            // we have fewer whole part digits than we need
            if (3.divides(wholeDigitNumber++)
                    && wholeDigitNumber != 1) {
                result.append(thousands);
            }
            result.appendCharacter('0');
        }
        while (integer != 0) {
            // emit whole part
            if (3.divides(wholeDigitNumber++)
                    && wholeDigitNumber != 1) {
                result.append(thousands);
            }
            value digit = integer % 10;
            integer /= 10;
            result.appendCharacter('0'.neighbour(digit));
        }
    }
    if (float < 0.0 && result.containsAny('1'..'9')) {
        result.appendCharacter('-');
    }
    return(result.string.reversed);
}

Integer exponent(variable Float f)
    =>  let (l10 = log10(f.magnitude))
        // now, compute the floor
        if (l10.fractionalPart == 0.0 || l10 > 0.0)
        then l10.wholePart.integer
        else l10.wholePart.integer - 1;

Float scaleByPowerOfTen(Float float, variable Integer power) {
    function doScale(Float float, Integer power) {
        value scale =
                let (magnitude = power.magnitude)
                if (magnitude <= 15)
                    then (10^magnitude).nearestFloat     //fast
                    else 10.0.powerOfInteger(magnitude); //slow
        return if (power < 0)
                then float / scale
                else float * scale;
    }
    // don't attempt to create a float larger than 1.0e308.
    variable value result = float;
    while (power > 0) {
        value amount = smallest(308, power);
        result = doScale(result, amount);
        power -= amount;
    }
    while (power < 0) {
        value amount = largest(-308, power);
        result = doScale(result, amount);
        power -= amount;
    }
    return result;
}

Float twoFiftyTwo = (2^52).float;

Float halfEven(Float num) {
    if (num.infinite ||
            num.undefined ||
            num.fractionalPart == 0.0) {
        return num;
    }

    variable value result = num.magnitude;
    if (result >= twoFiftyTwo) {
        return num;
    }

    // else, round
    result = (twoFiftyTwo + result) - twoFiftyTwo;
    return result * num.sign.float;
}

native
Float log10(Float num);

native("jvm")
Float log10(Float num) {
    import java.lang {
        Math
    }
    
    return Math.log10(num);
}

native("js")
Float log10(Float num) {
    dynamic {
        Float n = Math.log(num);
        Float d = Math.\iLN10;
        return n / d;
    }
}
