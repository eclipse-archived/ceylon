void testIntegerOperators() {
    
    variable Integer i1 := -4;
    i1 := -i1;
    i1 := +987654;
    i1 := +0;
    i1 := -0;
    
    variable Integer i2 := 123 + 456;
    i1 := i2 - 16;
    i2 := -i1 + i2 - 1;
    
    i1 := 3 * 7;
    i2 := i1 * 2;
    i2 := 17 / 4;
    i1 := i2 * 516 / -i1;
}

void testFloatOperators() {
    
    variable Float f1 := -4.2;
    f1 := -f1;
    f1 := +987654.9925567;
    f1 := +0.0;
    f1 := -0.0;
    
    variable Float f2 := 3.14159265 + 456.0;
    f1 := f2 - 0.0016;
    f2 := -f1 + f2 - 1.2;
    
    f1 := 3.0 * 0.79;
    f2 := f1 * 2.0e13;
    f2 := 17.1 / 4.0E-18;
    f1 := f2 * 51.6e2 / -f1;
}

class OpTest1() {}

void testBooleanOperators() {
    value o1 = OpTest1();
    value o2 = OpTest1();
    variable Boolean b1 := o1 === o2;
    variable Boolean b2 := o1 === o1;

    b1 := o1 == o2;
    b2 := o1 == o1;    
    b1 := 1 == 2;
    b2 := 1 != 2;
    variable Boolean b3 := !b2;
    
    b1 := true && false;
    b2 := b1 && true;
    b3 := true && true;
    b1 := true || false;
    b2 := false || b1;
    b3 := false || false;
}

void testComparisonOperators() {
    Comparison c1 = "str1" <=> "str2";
    Comparison c2 = "str2" <=> "str1";
    Comparison c3 = "str1" <=> "str1";
    Comparison c4 = "" <=> "";
    Comparison c5 = "str1" <=> "";
    Comparison c6 = "" <=> "str2";
    
    variable Boolean b1 := "str1" < "str2";
    variable Boolean b2 := "str1" > "str2";
    variable Boolean b3 := "str1" <= "str2";
    variable Boolean b4 := "str1" >= "str2";
    b1 := "str1" < "str1";
    b2 := "str1" > "str1";
    b3 := "str1" <= "str1";
    b4 := "str1" >= "str1";
}