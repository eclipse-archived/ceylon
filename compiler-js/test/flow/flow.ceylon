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
}

shared void test() {
    print("--- Start flow control tests ---");
    test_if();
    print("--- End flow control tests ---");
}
