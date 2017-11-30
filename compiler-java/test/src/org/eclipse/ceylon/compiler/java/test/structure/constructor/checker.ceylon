object checker {
    variable String[] s = [];
    shared void reset() {
        s=[];
    }
    shared void note(String str) {
        print(str);
        s = [str, *s];
    }
    shared void check(String expect) {
        value r = s.reversed.string;
        if (expect != r) {
            print("expected ``expect`` 
                   but got  ``r``");
            throw AssertionError("Expected ``expect`` but got ``r``");
        } else {
            print("got ``r``");
        }
    }
}

