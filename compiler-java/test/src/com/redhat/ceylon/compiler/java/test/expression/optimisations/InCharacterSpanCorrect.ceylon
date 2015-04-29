shared void inCharacterSpanCorrect() {
    void test(Character x, Character y, Character z) {
        variable Throwable? t1 = null;
        variable Boolean|String expect = "broken";
        try {
            Character[] it = y..z;
            expect = x in it;
        }
        catch (Throwable t) {
            t1 = t;
        }
        variable Throwable? t2 = null;
        variable Boolean|String got = "also broken";
        try {
            got = x in y..z;
        } 
        catch (Throwable t) {
            t2=t;
        }
        
        if(exists e1=t1) {
            if (exists e2=t2) {
                if (e2.message != e1.message) {
                    throw Exception("``x`` in ``y``..``z``:` exception messages differ");
                } else {
                    print("``x`` in ``y``..``z`` throws ``e2``");
                }
            } else {
                throw Exception("``x`` in ``y``..``z``:` unoptimized threw but optimized operation did not");
            }
        } else {
            if (exists e2=t2) {
                throw Exception("``x`` in ``y``..``z``:` optimized threw but unoptimized operation did not");
            } else {
                if (expect != got) {
                    throw Exception("``x`` in ``y``..``z``: got optimized result ``got`` but expected ``expect``");
                } else {
                    print("``x`` in ``y``..``z`` == ``got``");
                }
            }
        }
        
    }
    test('`', 'a','c');
    test('a', 'a','c');
    test('b', 'a','c');
    test('c', 'a','c');
    test('d', 'a','c');
    
    test('`', 'c','a');
    test('a', 'c','a');
    test('b', 'c','a');
    test('c', 'c','a');
    test('d', 'c','a');
    
    test(0.character, 0.character, 0.character);
    test('a', 'a', 'a');
    
    value minCharacterValue = 0.character;
    value maxCharacterValue = #10FFFF.character;
    test(maxCharacterValue, maxCharacterValue, maxCharacterValue);
    test(minCharacterValue, minCharacterValue, minCharacterValue);
    test(maxCharacterValue, minCharacterValue, maxCharacterValue);
    test(minCharacterValue, minCharacterValue, maxCharacterValue);
    test(maxCharacterValue, maxCharacterValue, minCharacterValue);
    test(minCharacterValue, maxCharacterValue, minCharacterValue);
    
}
