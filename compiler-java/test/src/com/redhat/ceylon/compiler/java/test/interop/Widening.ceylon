import java.lang {
    JavaInteger=Integer {
        javaIntMaxValue = \iMAX_VALUE,
        javaIntMinValue = \iMIN_VALUE
    },
    JavaShort=Short {
        javaShortMaxValue = \iMAX_VALUE,
        javaShortMinValue = \iMIN_VALUE
    },
    JavaByte=Byte {
        javaByteMaxValue = \iMAX_VALUE,
        javaByteMinValue = \iMIN_VALUE
    },
    JavaFloat=Float {
        javaFloatMaxValue = \iMAX_VALUE,
        javaFloatMinValue = \iMIN_VALUE
    }
}
    
shared void run() {
    value copyIntMaxValue = javaIntMaxValue;
    value copyIntMinValue = javaIntMinValue;
    
    assert(0 == javaIntMaxValue.rightLogicalShift(32));
    assert(0 == copyIntMaxValue.rightLogicalShift(32));
    
    assert(8589934591 == javaIntMinValue.rightLogicalShift(31));
    assert(8589934591 == copyIntMinValue.rightLogicalShift(31));
    
    value x = javaIntMaxValue+javaIntMaxValue;
    assert(4294967294 == x);
    assert(65534 == javaShortMaxValue+javaShortMaxValue);
    print(javaShortMinValue);
    assert(-65536 == javaShortMinValue+javaShortMinValue);
    print(javaByteMaxValue+javaByteMaxValue);
    assert(254.byte ==javaByteMaxValue+javaByteMaxValue);
    print(javaByteMinValue+javaByteMinValue);
    assert(0.byte == javaByteMinValue+javaByteMinValue);
    assert(6.805646932770577E38 == javaFloatMaxValue+javaFloatMaxValue);
    
    assert(69.character.integer==69);
    
    
}