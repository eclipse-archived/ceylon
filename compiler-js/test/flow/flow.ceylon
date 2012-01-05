void test_if() {
    //True, with else
    if (true) {
        print("if(true) OK");
    } else {
        print("Never happen");
    }
    //False, with else
    if (false) {
        print("Never happen");
    } else {
        print("if(false) OK");
    }
    //without else
    if (2+2 == 4) {
        print("if without else OK");
    }
    //chained if's
    if (1+2 == 4) {
        print("can't happen");
    } else if (2+2 == 5) {
        print("No way");
    } else {
        print("Chained if's with else OK");
    }
    //chained if's without else
    if (1+2 == 4) {
        print("can't happen");
    } else if (2+2 == 4) {
        print("Chained if's without else OK");
    }
    //More complex conditions
    if (2>1 && 1<2) {
        print("if && OK");
    } else { print("if && FAIL"); }
    if ((1>2 || 1>0) && 1<2) {
        print("if (||)&& OK");
    } else { print("if (||)&& FAIL"); }
}

void test_while() {
    variable Integer i := 0;
    while (i < 2) {
        print("while OK");
        i := i+1;
    }
    while (i >= 2 && i < 4) {
        print("while OK");
        i := i+1;
    }
}

shared void test() {
    print("--- Start flow control tests ---");
    test_if();
    test_while();
    print("--- End flow control tests ---");
}
