import java.lang{
    ObjectArray,
    BooleanArray,
    ByteArray,
    ShortArray,
    IntArray,
    LongArray,
    FloatArray,
    DoubleArray,
    CharArray
}


T[] javaArrayInSpreadArgumentFunc<T>(Iterable<T> s) {
    return s.sequence();
}
void javaArrayInSpreadArgumentString() {
    ObjectArray<String> it = ObjectArray<String>(3);
    it[0] = "a";
    it[1] = "b";
    it[2] = "c";
    
    // positional spread
    assert(javaArrayInSpreadArgumentFunc(*it) == ["a", "b", "c"]);
    
    // named args spread (as sequenced arg)
    assert(javaArrayInSpreadArgumentFunc<String>{*it} == ["a", "b", "c"]);
    assert(javaArrayInSpreadArgumentFunc<String>{"z", *it} == ["z", "a", "b", "c"]);
    
    // iterable instantiation
    assert({*it}.sequence() == ["a", "b", "c"]);
    assert({"z", *it}.sequence() == ["z", "a", "b", "c"]);
    
    // tuple instantiation
    assert([*it] == ["a", "b", "c"]);
    assert(["z", *it] == ["z", "a", "b", "c"]);
}
void javaArrayInSpreadArgumentBoolean() {
    BooleanArray it = BooleanArray(3);
    it[0] = true;
    it[1] = false;
    it[2] = true;
    
    // positional spread
    assert(javaArrayInSpreadArgumentFunc(*it) == [true, false, true]);
    
    // named args spread (as sequenced arg)
    assert(javaArrayInSpreadArgumentFunc<Boolean>{*it} == [true, false, true]);
    assert(javaArrayInSpreadArgumentFunc<Boolean>{false, *it} == [false, true, false, true]);
    
    // iterable instantiation
    assert({*it}.sequence() == [true, false, true]);
    assert({false, *it}.sequence() == [false, true, false, true]);
    
    // tuple instantiation
    assert([*it] == [true, false, true]);
    assert([false, *it] == [false, true, false, true]);
}
void javaArrayInSpreadArgumentShort() {
    ShortArray it = ShortArray(3);
    it[0] = 1;
    it[1] = 2;
    it[2] = 3;
    
    // positional spread
    assert(javaArrayInSpreadArgumentFunc(*it) == [1, 2, 3]);
    
    // named args spread (as sequenced arg)
    assert(javaArrayInSpreadArgumentFunc<Integer>{*it} == [1, 2, 3]);
    assert(javaArrayInSpreadArgumentFunc<Integer>{0, *it} == [0, 1, 2, 3]);
    
    // iterable instantiation
    assert({*it}.sequence() == [1, 2, 3]);
    assert({0, *it}.sequence() == [0, 1, 2, 3]);
    
    // tuple instantiation
    assert([*it] == [1, 2, 3]);
    assert([0, *it] == [0, 1, 2, 3]);
}
void javaArrayInSpreadArgumentInt() {
    IntArray it = IntArray(3);
    it[0] = 1;
    it[1] = 2;
    it[2] = 3;
    
    // positional spread
    assert(javaArrayInSpreadArgumentFunc(*it) == [1, 2, 3]);
    
    // named args spread (as sequenced arg)
    assert(javaArrayInSpreadArgumentFunc<Integer>{*it} == [1, 2, 3]);
    assert(javaArrayInSpreadArgumentFunc<Integer>{0, *it} == [0, 1, 2, 3]);
    
    // iterable instantiation
    assert({*it}.sequence() == [1, 2, 3]);
    assert({0, *it}.sequence() == [0, 1, 2, 3]);
    
    // tuple instantiation
    assert([*it] == [1, 2, 3]);
    assert([0, *it] == [0, 1, 2, 3]);
}
void javaArrayInSpreadArgumentLong() {
    LongArray it = LongArray(3);
    it[0] = 1;
    it[1] = 2;
    it[2] = 3;
    
    // positional spread
    assert(javaArrayInSpreadArgumentFunc(*it) == [1, 2, 3]);
    
    // named args spread (as sequenced arg)
    assert(javaArrayInSpreadArgumentFunc<Integer>{*it} == [1, 2, 3]);
    assert(javaArrayInSpreadArgumentFunc<Integer>{0, *it} == [0, 1, 2, 3]);
    
    // iterable instantiation
    assert({*it}.sequence() == [1, 2, 3]);
    assert({0, *it}.sequence() == [0, 1, 2, 3]);
    
    // tuple instantiation
    assert([*it] == [1, 2, 3]);
    assert([0, *it] == [0, 1, 2, 3]);
}

void javaArrayInSpreadArgumentChar() {
    CharArray it = CharArray(3);
    it[0] = 'a';
    it[1] = 'b';
    it[2] = 'c';
    
    // positional spread
    assert(javaArrayInSpreadArgumentFunc(*it) == ['a', 'b', 'c']);
    
    // named args spread (as sequenced arg)
    assert(javaArrayInSpreadArgumentFunc<Character>{*it} == ['a', 'b', 'c']);
    assert(javaArrayInSpreadArgumentFunc<Character>{'z', *it} == ['z', 'a', 'b', 'c']);
    
    // iterable instantiation
    assert({*it}.sequence() == ['a', 'b', 'c']);
    assert({'z', *it}.sequence() == ['z', 'a', 'b', 'c']);
    
    // tuple instantiation
    assert([*it] == ['a', 'b', 'c']);
    assert(['z', *it] == ['z', 'a', 'b', 'c']);
}
void javaArrayInSpreadArgumentFloat() {
    FloatArray it = FloatArray(3);
    it[0] = 1.0;
    it[1] = 2.0;
    it[2] = 3.0;
    
    // positional spread
    assert(javaArrayInSpreadArgumentFunc(*it) == [1.0, 2.0, 3.0]);
    
    // named args spread (as sequenced arg)
    assert(javaArrayInSpreadArgumentFunc<Float>{*it} == [1.0, 2.0, 3.0]);
    assert(javaArrayInSpreadArgumentFunc<Float>{0.0, *it} == [0.0, 1.0, 2.0, 3.0]);
    
    // iterable instantiation
    assert({*it}.sequence() == [1.0, 2.0, 3.0]);
    assert({0.0, *it}.sequence() == [0.0, 1.0, 2.0, 3.0]);
    
    // tuple instantiation
    assert([*it] == [1.0, 2.0, 3.0]);
    assert([0.0, *it] == [0.0, 1.0, 2.0, 3.0]);
}
void javaArrayInSpreadArgumentDouble() {
    DoubleArray it = DoubleArray(3);
    it[0] = 1.0;
    it[1] = 2.0;
    it[2] = 3.0;
    
    // positional spread
    assert(javaArrayInSpreadArgumentFunc(*it) == [1.0, 2.0, 3.0]);
    
    // named args spread (as sequenced arg)
    assert(javaArrayInSpreadArgumentFunc<Float>{*it} == [1.0, 2.0, 3.0]);
    assert(javaArrayInSpreadArgumentFunc<Float>{0.0, *it} == [0.0, 1.0, 2.0, 3.0]);
    
    // iterable instantiation
    assert({*it}.sequence() == [1.0, 2.0, 3.0]);
    assert({0.0, *it}.sequence() == [0.0, 1.0, 2.0, 3.0]);
    
    // tuple instantiation
    assert([*it] == [1.0, 2.0, 3.0]);
    assert([0.0, *it] == [0.0, 1.0, 2.0, 3.0]);
}
void javaArrayInSpreadArgumentByte() {
    ByteArray it = ByteArray(3);
    it[0] = 1.byte;
    it[1] = 2.byte;
    it[2] = 3.byte;
    
    // positional spread
    assert(javaArrayInSpreadArgumentFunc(*it) == [1.byte, 2.byte, 3.byte]);
    
    // named args spread (as sequenced arg)
    assert(javaArrayInSpreadArgumentFunc<Byte>{*it} == [1.byte, 2.byte, 3.byte]);
    assert(javaArrayInSpreadArgumentFunc<Byte>{0.byte, *it} == [0.byte, 1.byte, 2.byte, 3.byte]);
    
    // iterable instantiation
    assert({*it}.sequence() == [1.byte, 2.byte, 3.byte]);
    assert({0.byte, *it}.sequence() == [0.byte, 1.byte, 2.byte, 3.byte]);
    
    // tuple instantiation
    assert([*it] == [1.byte, 2.byte, 3.byte]);
    assert([0.byte, *it] == [0.byte, 1.byte, 2.byte, 3.byte]);
}
void javaArrayInSpreadArgument() {
    javaArrayInSpreadArgumentString();
    javaArrayInSpreadArgumentBoolean();
    javaArrayInSpreadArgumentShort();
    javaArrayInSpreadArgumentInt();
    javaArrayInSpreadArgumentLong();
    
    javaArrayInSpreadArgumentChar();
    
    javaArrayInSpreadArgumentFloat();
    javaArrayInSpreadArgumentDouble();
    
    javaArrayInSpreadArgumentByte();
    
}