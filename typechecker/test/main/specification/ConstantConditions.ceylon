class ConstantConditions() {
    void ifTrue() {
        String s;
        if (true) {
            s = "definitely";
        }
        print(s);
    }
    void ifFalse() {
        String s;
        if (false) {} else {
            s = "definitely";
        }
        print(s);
    }
    void ifElseAssert() {
        String s;
        if (1==1) {
            s = "definitely";
        }
        else {
            assert (false);
        }
        print(s);
    }
    void ifAssertElse() {
        String s;
        if (1==1) {
            assert (false);
        }
        else {
            s = "definitely";
        }
        print(s);
    }
    void whileFalse() {
        variable String s;
        while (false) {
            s = "xxx";
        }
        s = "definitely";
    }
}