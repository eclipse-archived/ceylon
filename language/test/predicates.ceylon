@test
shared void testPredicates() {
    Boolean hell(String s) => s.startsWith("hell");
    Boolean llo(String s) => s.endsWith("llo");
    
    check(!not(hell)("hell"), "not/hell");
    check(!not(hell)("hello"), "not/hello");
    check(not(hell)("h"), "not/h");
    check(not(hell)("hel"), "not/hel");
    
    check(and(hell, llo)("hello"), "and/hello");
    check(and(hell, llo)("hellxllo"), "and/hellxllo");
    check(!and(hell, llo)("helo"), "and/helo");
    check(!and(hell, llo)("hell"), "and/hell");
    check(!and(hell, llo)("llo"), "and/llo");
    
    check(or(hell, llo)("hello"), "or/hello");
    check(or(hell, llo)("hell"), "or/hell");
    check(or(hell, llo)("hellx"), "or/hellx");
    check(or(hell, llo)("llo"), "or/llo");
    check(or(hell, llo)("xllo"), "or/xllo");
    check(!or(hell, llo)("x"), "or/x");
    check(!or(hell, llo)("llohell"), "or/llohell");
    
    check(!xor(hell, llo)("hello"), "xor/hello");
    check(xor(hell, llo)("hell"), "xor/hell");
    check(xor(hell, llo)("hellx"), "xor/hellx");
    check(xor(hell, llo)("llo"), "xor/llo");
    check(xor(hell, llo)("xllo"), "xor/xllo");
    check(!xor(hell, llo)("x"), "xor/x");
    check(!xor(hell, llo)("llohell"), "xor/llohell");
    
}