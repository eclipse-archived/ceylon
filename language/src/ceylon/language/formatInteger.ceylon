"The string representation of the given [[integer]] in the 
 base given by [[radix]]. If the given integer is negative, 
 the string representation will begin with `-`. Digits 
 consist of decimal digits `0` to `9`, together with and 
 lowercase letters `a` to `z` for bases greater than 10.
 
 For example:
 
 - `formatInteger(-46)` is `\"-46\"`
 - `formatInteger(9,2)` is `\"1001\"`
 - `formatInteger(10,8)` is `\"12\"`
 - `formatInteger(511,16)` is `\"1ff\"`
 - `formatInteger(512,32)` is `\"g0\"`"
throws (`class AssertionError`, 
        "if [[radix]] is not between [[minRadix]] and 
         [[maxRadix]]")
see (`function parseInteger`)
shared String formatInteger(
            "The integer value to format."
            Integer integer,
            "The base, between [[minRadix]] and [[maxRadix]] 
             inclusive."
            Integer radix = 10) {
    assert (radix >= minRadix, radix <= maxRadix);
    if (integer == 0) {
        return "0";
    }
    StringBuilder digits = StringBuilder();
    Integer insertIndex;
    variable Integer i;
    if (integer < 0) {
        digits.append("-");
        insertIndex = 1;
        i = integer;
    } else {
        insertIndex = 0;
        i = -integer;
    }
    while (i != 0) {
        Integer d = -(i % radix);
        Character c;
        if (0<=d<10) {
            c = (d+zeroInt).character;
        }
        else if (10<=d<36) {
            c = (d-10+aIntLower).character;
        }
        else {
            assert (false);
        }
        digits.insertCharacter(insertIndex, c);
        i = (i + d) / radix;
    }
    return digits.string;
}
