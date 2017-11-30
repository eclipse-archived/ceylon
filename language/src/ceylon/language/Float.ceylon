/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"An IEEE 754 64-bit [floating point number][]. A `Float` is 
 capable of approximately representing numeric values 
 between:
 
 - 2<sup>-1022</sup>, approximately 
   1.79769\{#00D7}10<sup>308</sup>, and 
 - (2-2<sup>-52</sup>)\{#00D7}2<sup>1023</sup>, 
   approximately 5\{#00D7}10<sup>-324</sup>.
 
 Zero is represented by distinct instances `+0.0`, `-0.0`, 
 but these instances are equal. `-0.0` can be distinguished
 from `+0.0` using `f == 0.0 && f.strictlyNegative`.
 
 In addition, the following special values exist:
 
 - [[infinity]] and `-infinity`, and
 - [[undefined values|undefined]], denoted [NaN][] by the
   IEEE standard.
 
 As required by the IEEE standard no undefined value is 
 equal to any other value, nor even to itself. Thus, the 
 definition of [[equals]] for `Float` violates the general 
 contract defined by [[Object.equals]].
 
 A floating point value with a zero [[fractionalPart]] is
 considered equal to its [[integer]] part.
 
 Literal floating point values are written with a decimal
 point and, optionally, a magnitude or exponent:
 
     1.0
     1.0E6
     1.0M
     1.0E-6
     1.0u
 
 In the case of a fractional magnitude, the decimal point is
 optional. Underscores may be used to group digits into 
 groups of three.
 
 [floating point number]: http://www.validlab.com/goldberg/paper.pdf
 [NaN]: http://en.wikipedia.org/wiki/NaN"
tagged("Basic types", "Numbers")
shared native final class Float
        extends Object
        satisfies Number<Float> 
                & Exponentiable<Float,Float> {
    
    "The sum of all the floating point values in the given 
     stream, `0.0` if the stream is empty, or an 
     [[undefined]] value if and only if the stream contains 
     an undefined value.
     
     This expression produces the mean of a list of floating
     point values:
     
         Float.sum(list) / list.size"
    since("1.3.2")
    shared static Float sum({Float*} floats) {
        variable value sum = 0.0;
        for (float in floats) {
            sum += float;
        }
        return sum;
    }
    
    "The product of all the floating point values in the 
     given stream, `1.0` if the stream is empty, or an 
     [[undefined]] value if and only if the stream contains 
     an undefined value.
     
     This expression produces the geometric mean of a list 
     of floating point values:
     
         Float.product(list) ^ (1.0/list.size)"
    since("1.3.2")
    shared static Float product({Float*} floats) {
        variable value product = 1.0;
        for (float in floats) {
            product *= float;
        }
        return product;
    }
    
    "The largest floating point value in the given stream, 
     `null` if the stream is empty, or an [[undefined]] 
     value if and only if the stream contains an undefined 
     value."
    since("1.3.2")
    shared static Float|Absent max<Absent>
            (Iterable<Float,Absent> floats)
            given Absent satisfies Null {
        variable value max = 0.0/0.0;
        for (x in floats) {
            if (x.undefined 
                || x==infinity) {
                return x;
            }
            if (max.undefined
                || x > max 
                || x.strictlyPositive 
                && max.strictlyNegative) {
                max = x;
            }
        }
        if (max.undefined) {
            assert (is Absent null);
            return null; 
        }
        else {
            return max;
        }
    }
    
    "The smallest floating point value in the given stream, 
     `null` if the stream is empty, or an [[undefined]] 
     value if and only if the stream contains an undefined 
     value."
    since("1.3.2")
    shared static Float|Absent min<Absent>
            (Iterable<Float,Absent> floats)
            given Absent satisfies Null {
        variable value min = 0.0/0.0;
        for (x in floats) {
            if (x.undefined 
                || x==-infinity) {
                return x;
            }
            if (min.undefined
                || x < min 
                || x.strictlyNegative 
                && min.strictlyPositive) {
                min = x;
            }
        }
        if (min.undefined) {
            assert (is Absent null);
            return null; 
        }
        else {
            return min;
        }
    }
    
    "The smaller of the two given floating point values, or
     an [[undefined]] value if and only if one of the values
     is undefined."
    since("1.3.2")
    shared static Float smallest(Float x, Float y)
            => if (x.strictlyNegative && y.strictlyPositive)
                then x
            else if (x.strictlyPositive && y.strictlyNegative)
                then y
            else if (x.undefined)
                then x
            else if (y.undefined)
                then y
            else if (x<y) then x else y;
    
    "The larger of the two given floating point values, or
     an [[undefined]] value if and only if one of the values
     is undefined."
    since("1.3.2")
    shared static Float largest(Float x, Float y)
            => if (x.strictlyNegative && y.strictlyPositive)
                then y
            else if (x.strictlyPositive && y.strictlyNegative)
                then x
            else if (x.undefined)
                then x
            else if (y.undefined)
                then y
            else if (x>y) then x else y;
    
    "The [[Float]] value of the given 
     [[string representation|string]] of a decimal floating 
     point number, or `null` if the string does not 
     represent a decimal floating point number.
     
     If the given string representation contains more digits
     than can be represented by a `Float`, then the least 
     significant digits are ignored.
     
     The syntax accepted by this method is the same as the 
     syntax for a `Float` literal in the Ceylon language 
     except that it may optionally begin with a sign 
     character (`+` or `-`) and may not contain grouping 
     underscore characters. That is, an optional sign 
     character, followed by a string of decimal digits, 
     followed by an optional decimal point and string of 
     decimal digits, followed by an optional decimal 
     exponent, for example, `e+10` or `E-5`, or SI magnitude, 
     `k`, `M`, `G`, `T`, `P`, `m`, `u`, `n`, `p`, or `f`.
     
     Float: Sign? Digits ('.' Digits)? (Magnitude|Exponent)
     Sign: '+' | '-'
     Magnitude: 'k' | 'M' | 'G' | 'T' | 'P' | 'm' | 'u' | 'n' | 'p' | 'f'
     Exponent: ('e'|'E') Sign? Digits
     Digits: ('0'..'9')+"
    see (function format, function Integer.parse)
    tagged("Numbers", "Basic types")
    since("1.3.1")
    shared static Float|ParseException parse(String string)
            => parseFloatInternal(string);
    
    "The string decimal representation of the given 
     [[floating point number|float]]. If the given number is 
     [[negative|Float.negative]], the string representation 
     will begin with `-`. The [[whole part|Float.wholePart]] 
     and [[fractional parts|Float.fractionalPart]] of the 
     number are separated by a `.` decimal point. Digits 
     consist of decimal digits `0` to `9`. 
     
     The number of decimal places following the decimal 
     point is controlled by the parameters 
     [[minDecimalPlaces]] and [[maxDecimalPlaces]], which 
     default to `1` and `9` respectively, so that by default 
     the string representation always contains a decimal 
     point, and never contains more than nine decimal places. 
     The decimal representation is rounded so that the 
     number of decimal places never exceeds the specified 
     maximum.
     
     For example:
     
     - `Float.format(1234.1234)` is `\"1234.1234\"`
     - `Float.format(0.1234)` is `\"0.1234\"`
     - `Float.format(1234.0)` is `\"1234.0\"`
     - `Float.format(1234.0,0)` is `\"1234\"`
     - `Float.format(1234.1234,6)` is `\"1234.123400\"`
     - `Float.format(1234.1234,0,2)` is `\"1234.12\"`
     - `Float.format(1234.123456,0,5)` is `\"1234.12346\"`
     - `Float.format(0.0001,2,2)` is `\"0.00\"`
     - `Float.format(0.0001,0,2)` is `\"0\"`
     
     Finally:
     
     - `Float.format(-0.0)` is `\"0.0\"`,
     - `Float.format(0.0/0)` is `\"NaN\"`,
     - `Float.format(1.0/0)` is `\"Infinity\"`, and
     - `Float.format(-1.0/0)` is `\"-Infinity\".`
     
     This function never produces a representation involving 
     scientific notation."
    tagged("Numbers")
    see (function parse, function Integer.format)
    since("1.3.1")
    shared static String format(
        "The floating point value to format."
        Float float,
        "The minimum number of allowed decimal places.
         
         If `minDecimalPlaces<=0`, the result may have no
         decimal point."
        Integer minDecimalPlaces=1,
        "The maximum number of allowed decimal places.
         
         If `maxDecimalPlaces<=0`, the result always has no
         decimal point."
        Integer maxDecimalPlaces=9,
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
        Character? thousandsSeparator = null)
            => package.formatFloat(float, 
                minDecimalPlaces, maxDecimalPlaces, 
                decimalSeparator, thousandsSeparator);
    
    shared new(Float float) extends Object() {}
    
    "Determines whether this value is undefined. The IEEE
     standard denotes undefined values [NaN][] (an 
     abbreviation of Not a Number). Undefined values include:
     
     - _indeterminate forms_ including `0.0/0.0`, 
       `infinity/infinity`, `0.0*infinity`, and
       `infinity-infinity`, along with
     - _complex numbers_ like `sqrt(-1.0)` and `log(-1.0)`.
     
     An undefined value has the property that it is not 
     [[equal|Object.equals]] (`==`) to itself, and as a 
     consequence the undefined value cannot sensibly be used 
     in most collections.
     
     If `x` is an undefined `Float`, then:
     
     - `x==x` evaluates to `false`
     - `x!=x` evaluates to `true`, and
     - `x>x`, `x<x`, `x>=x`, `x<=x` all evaluate to `false`.
     
     [NaN]: http://en.wikipedia.org/wiki/NaN"
    see (function compare)
    aliased("notANumber")
    shared Boolean undefined => this!=this;
    
    "Determines whether this value is infinite in magnitude. 
     Produces `true` for `infinity` and `-infinity`. 
     Produces `false` for a finite number, `+0.0`, `-0.0`, 
     or undefined."
    see (value infinity, value finite)
    shared Boolean infinite 
            => this==infinity || this==-infinity;
    
    "Determines whether this value is finite. Produces
     `false` for `infinity`, `-infinity`, and undefined."
    see (value infinite, value infinity)
    shared Boolean finite 
            => this!=infinity && this!=-infinity 
                    && !this.undefined;
    
    "The sign of this value. Produces `1` for a positive 
     number or `infinity`. Produces `-1` for a negative
     number or `-infinity`. Produces `0.0` for `+0.0`, 
     `-0.0`, or undefined."
    shared actual native Integer sign
            =>   if (this < 0.0) then -1
            else if (this > 0.0) then 1
            else 0;
    
    "Determines if this value is a positive number or
     `infinity`. Produces `false` for a negative number, 
     `+0.0`, `-0.0`, or undefined."
    shared actual native Boolean positive => this > 0.0;
    
    "Determines if this value is a negative number or
     `-infinity`. Produces `false` for a positive number, 
     `+0.0`, `-0.0`, or undefined."
    shared actual native Boolean negative => this < 0.0;
    
    "Determines if this value is a positive number, `+0.0`, 
     or `infinity`. Produces `false` for a negative number, 
     `-0.0`, or undefined."
    shared native Boolean strictlyPositive 
            => this > 0.0 || 1.0/this > 0.0;
    
    "Determines if this value is a negative number, `-0.0`, 
     or `-infinity`. Produces `false` for a positive number, 
     `+0.0`, or undefined."
    shared native Boolean strictlyNegative 
            => this < 0.0 || 1.0/this < 0.0;
    
    "Determines if the given object is equal to this `Float`,
     that is, if:
     
     - the given object is also a `Float`,
     - neither this value nor the given value is 
       [[undefined]], and either
     - both values are [[infinite]] and have the same 
       [[sign]], or both represent the same finite floating 
       point value as defined by the IEEE specification.
     
     Or if:
     
     - the given object is an [[Integer]],
     - this value is neither [[undefined]], nor [[infinite]],
     - the [[fractionalPart]] of this value equals `0.0`, 
     - the [[integer]] part of this value equals the given 
       integer, and
     - the given integer is between -2<sup>53</sup> and 
       2<sup>53</sup> (exclusive)."
    shared actual native Boolean equals(Object that);
    
    "A platform-dependent hash code for this `Float`."
    shared actual native Integer hash;
    
    "Compare this value to the given value, where 
     [[infinity]] is considered greater than every defined, 
     finite value, and `-infinity` is considered smaller 
     than every defined, finite value, and [[undefined]] 
     values are considered incomparable.
     
     Note that if `x` is an undefined `Float` and `y` is any 
     `Float` that is not undefined, then:
     
     - `x<=>y` produces an exception when evaluated, but
     - `x>y`, `x<y`, `x>=y`, `x<=y`, and `x==y` all evaluate 
       to `false`."
    throws (class Exception, 
            "if either this value, the given value, or both 
             are [[undefined]]")
    shared actual native Comparison compare(Float other)
            =>   if (this < other) then smaller
            else if (this > other) then larger
            else equal;
    
    shared actual native Float negated;
    
    shared actual native Float plus(Float other);
    shared actual native Float minus(Float other);
    shared actual native Float times(Float other);
    shared actual native Float divided(Float other);
    
    "The result of raising this number to the given floating
     point power, where, following the definition of the
     IEEE `pow()` function, the following indeterminate 
     forms all evaluate to `1.0`:
     
     - `0.0^0.0`,
     - `infinity^0.0` and `(-infinity)^0.0`, 
     - `1.0^infinity` and `(-1.0)^infinity`.
     
     Furthermore:
     
      - `0.0^infinity` evaluates to `0.0`, and
      - `0.0^(-infinity)` evaluates to `infinity`.
     
     If this is a [[negative]] number, and the given 
     [[power|other]] has a nonzero [[fractionalPart]], the 
     result is [[undefined]].
     
     For any negative power `y<0.0`:
     
     - `0.0^y` evaluates to `infinity`,
     - `(-0.0)^y` evaluates to `-infinity`, and
     - for any nonzero floating point number `x`, `x^y` 
       evaluates to `1.0/x^(-y)`."
    shared actual native Float power(Float other);
    
    shared actual native Float wholePart;
    shared actual native Float fractionalPart;
    
    aliased("absolute")
    shared actual native Float magnitude 
            => this <= 0.0 then 0.0-this else this;
    
    "This value, represented as an [[Integer]], after 
     truncation of its fractional part, if such a 
     representation is possible."
    throws (class OverflowException,
        "if the the [[wholePart]] of this value is too large 
         or too small to be represented as an `Integer`")
    since("1.1.0")
    shared native Integer integer;
    
    shared actual native Float timesInteger(Integer integer)
            => times(integer.nearestFloat);
    
    shared actual native Float plusInteger(Integer integer)
            => plus(integer.nearestFloat);
    
    "The result of raising this number to the given integer
     power, where the following indeterminate forms evaluate 
     to `1.0`:
     
     - `0.0^0`,
     - `infinity^0` and `(-infinity)^0`.
     
     For any negative integer power `n<0`:
     
     - `0.0^n` evaluates to `infinity`,
     - `(-0.0)^n` evaluates to `-infinity`, and
     - for any nonzero floating point number `x`, `x^n` 
       evaluates to `1.0/x^(-n)`."
    shared actual native Float powerOfInteger(Integer integer);
    
    "A string representing this floating point number.
     
     - `\"NaN\"`, for any [[undefined value|undefined]]
     - `\"Infinity\"`, for [[infinity]], 
     - `\"-Infinity\"`, for [[-infinity]], or,
     - a Ceylon floating point literal that evaluates to 
       this floating point number, for example, `\"1.0\"`, 
       `\"-0.0\"`, or `\"1.5E10\"`."
    see (function formatFloat)
    shared actual native String string;
    
    "Determines if this value is strictly larger than the 
     given value, where [[infinity]] is considered greater 
     than every defined, finite value, and `-infinity` is 
     considered smaller than every defined, finite value. 
     Evaluates to `false` if this value, the given value, or 
     both are [[undefined]]."
    shared actual native Boolean largerThan(Float other);
    
    "Determines if this value is strictly smaller than the 
     given value, where [[infinity]] is considered greater 
     than every defined, finite value, and `-infinity` is 
     considered smaller than every defined, finite value. 
     Evaluates to `false` if this value, the given value, or 
     both are [[undefined]]." 
    shared actual native Boolean smallerThan(Float other); 
    
    "Determines if this value is larger than or equal to the 
     given value, where [[infinity]] is considered greater 
     than every defined, finite value, and `-infinity` is 
     considered smaller than every defined, finite value. 
     Evaluates to `false` if this value, the given value, or 
     both are [[undefined]]."
    shared actual native Boolean notSmallerThan(Float other);
    
    "Determines if this value is smaller than or equal to the 
     given value, where [[infinity]] is considered greater 
     than every defined, finite value, and `-infinity` is 
     considered smaller than every defined, finite value. 
     Evaluates to `false` if this value, the given value, or 
     both are [[undefined]]." 
    shared actual native Boolean notLargerThan(Float other); 
}
