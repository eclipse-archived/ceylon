shared void inCharacterMeasureCorrect() {
    void test(Character x, Character y, Integer z) {
        variable Throwable? t1 = null;
        variable Boolean|String expect = "broken";
        try {
            Character[] it = y:z;
            expect = x in it;
        }
        catch (Throwable t) {
            t1 = t;
        }
        variable Throwable? t2 = null;
        variable Boolean|String got = "also broken";
        try {
            got = x in y:z;
        } 
        catch (Throwable t) {
            t2=t;
        }
        
        if(exists e1=t1) {
            if (exists e2=t2) {
                if (e2.message != e1.message) {
                    throw Exception("``x`` in ``y``:``z``:` exception messages differ");
                } else {
                    print("``x`` in ``y``:``z`` throws ``e2``");
                }
            } else {
                throw Exception("``x`` in ``y``:``z``:` unoptimized threw but optimized operation did not");
            }
        } else {
            if (exists e2=t2) {
                throw Exception("``x`` in ``y``:``z``:` optimized threw but unoptimized operation did not");
            } else {
                if (expect != got) {
                    throw Exception("``x`` in ``y``:``z``: got optimized result ``got`` but expected ``expect``");
                } else {
                    print("``x`` in ``y``:``z`` == ``got``");
                }
            }
        }
        
    }
    test('`', 'a', 3);
    test('a', 'a', 3);
    test('b', 'a', 3);
    test('c', 'a', 3);
    test('d', 'a', 3);
    
    test('a', 'a', 0);
    
    test(#10FFFF.character, #10FFFF.character, 1);
    test(#10FFFF.character, #10FFFF.character, 2);
    test(0.character, #10FFFF.character, 2);
    test(0.character, #10FFFF.character, #10FFFF);
}
