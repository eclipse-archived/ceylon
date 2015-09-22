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
 truncated so that the number of decimal places never 
 exceeds the specified maximum.
 
 For example:
 
 - `formatFloat(1234.1234)` is `\"1234.1234\"`
 - `formatFloat(0.1234)` is `\"0.1234\"`
 - `formatFloat(1234.0)` is `\"1234.0\"`
 - `formatFloat(1234.0,0)` is `\"1234\"`
 - `formatFloat(1234.1234,6)` is `\"1234.123400\"`
 - `formatFloat(1234.1234,0,2)` is `\"1234.12\"`
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
shared String formatFloat(
        "The floating point value to format."
        Float float,
        "The minimum number of allowed decimal places.
         
         If `minDecimalPlaces<=0`, the result may have no
         decimal point."
        Integer minDecimalPlaces=1,
        "The maximum number of allowed decimal places.
         
         If `maxDecimalPlaces<=0`, the result always has no
         decimal point."
        Integer maxDecimalPlaces=9) {
    if (float.undefined || float.infinite) {
        return float.string;
    }
    variable {Character*} digits = {};
    variable Integer i = maxDecimalPlaces;
    Float m = float.magnitude;
    while (true) {
        Float f = m * 10.0.powerOfInteger(--i); //really slow for large i
        Float d = (f.fractionalPart * 10).wholePart;
        Character c = (d.integer+zeroInt).character;
        digits = digits.follow(c);
        if (f.wholePart==0.0) {
            break;
        }
    }
    String string = String(digits);
    Integer point = string.size - maxDecimalPlaces;
    String wholePart;
    String fractionalPart;
    if (point>0) {
        wholePart = string[0:point];
        fractionalPart = string[point...];
    }
    else {
        wholePart = "0"; 
        fractionalPart 
                = string.padLeading(maxDecimalPlaces, '0');
    }
    
    String normalized = 
            fractionalPart
                .trimTrailing('0'.equals)
                .padTrailing(minDecimalPlaces, '0');
    String signed = 
            float.negative
                then "-" + wholePart
                else wholePart;
    return normalized.empty 
            then signed 
            else signed + "." + normalized;
}