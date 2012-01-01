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

void testBooleanOperators() {
    Boolean b1 = 1 == 2;
    Boolean b2 = 1 != 2;
    Boolean b3 = !b2;
}