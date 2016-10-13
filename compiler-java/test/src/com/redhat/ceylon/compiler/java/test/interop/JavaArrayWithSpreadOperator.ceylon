import java.lang{
    ObjectArray,
    ByteArray,
    BooleanArray,
    ShortArray,
    IntArray,
    LongArray,
    FloatArray,
    DoubleArray,
    CharArray
}


void javaArrayWithSpreadOperatorString() {
    value it = ObjectArray<String>(3);
    it[0] = "a";
    it[1] = "b";
    it[2] = "c";
    
    assert(it*.uppercased == ["A", "B", "C"]);
    assert(it*.compare("b") == [smaller, equal, larger]);
    assert(it*.uppercased*.compare("B") == [smaller, equal, larger]);
}
void javaArrayWithSpreadOperatorBoolean() {
    value it = BooleanArray(3);
    it[0] = true;
    it[1] = false;
    it[2] = true;
    
    assert(it*.string == ["true", "false", "true"]);
    assert(it*.equals(false) == [false, true, false]);
}
void javaArrayWithSpreadOperatorShort() {
    value it = ShortArray(3);
    it[0] = 1;
    it[1] = 2;
    it[2] = 3;
    
    assert(it*.string == ["1", "2", "3"]);
    assert(it*.plus(1) == [2, 3, 4]);
}
void javaArrayWithSpreadOperatorInt() {
    value it = IntArray(3);
    it[0] = 1;
    it[1] = 2;
    it[2] = 3;
    
    assert(it*.string == ["1", "2", "3"]);
    assert(it*.plus(1) == [2, 3, 4]);
}
void javaArrayWithSpreadOperatorLong() {
    value it = LongArray(3);
    it[0] = 1;
    it[1] = 2;
    it[2] = 3;
    
    assert(it*.string == ["1", "2", "3"]);
    assert(it*.plus(1) == [2, 3, 4]);
}
void javaArrayWithSpreadOperatorByte() {
    value it = ByteArray(3);
    it[0] = 1.byte;
    it[1] = 2.byte;
    it[2] = 3.byte;
    
    assert(it*.string == ["1", "2", "3"]);
    assert(it*.equals(2.byte) == [false, true, false]);
}
void javaArrayWithSpreadOperatorFloat() {
    value it = FloatArray(3);
    it[0] = 1.0;
    it[1] = 2.0;
    it[2] = 3.0;
    
    assert(it*.string == ["1.0", "2.0", "3.0"]);
    assert(it*.plus(1.0) == [2.0, 3.0, 4.0]);
}
void javaArrayWithSpreadOperatorDouble() {
    value it = DoubleArray(3);
    it[0] = 1.0;
    it[1] = 2.0;
    it[2] = 3.0;
    
    assert(it*.string == ["1.0", "2.0", "3.0"]);
    assert(it*.plus(1.0) == [2.0, 3.0, 4.0]);
}
void javaArrayWithSpreadOperator() {
    javaArrayWithSpreadOperatorString();
    javaArrayWithSpreadOperatorBoolean();
    javaArrayWithSpreadOperatorByte();
    javaArrayWithSpreadOperatorShort();
    javaArrayWithSpreadOperatorInt();
    javaArrayWithSpreadOperatorLong();
    javaArrayWithSpreadOperatorFloat();
    javaArrayWithSpreadOperatorDouble();
    //javaArrayWithSpreadOperatorChar();
    
}