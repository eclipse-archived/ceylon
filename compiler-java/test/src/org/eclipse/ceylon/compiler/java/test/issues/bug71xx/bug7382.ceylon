void test7832(Number<out Anything> num) {
    Boolean isIntegral1 = num is Integral<out Anything>;
    Boolean isIntegral2 = num is Integral<in Nothing>;
    Boolean isInteger = num is Integral<Integer>;
    Boolean isAnything = num is Integral<Anything>;
    Boolean isNothing = num is Integral<Nothing>;
}