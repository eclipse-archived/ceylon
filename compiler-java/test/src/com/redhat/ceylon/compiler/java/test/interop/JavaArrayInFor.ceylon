import java.lang{
    JString=String,
    
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

@noanno
void javaArrayInFor() {
    variable value sum = 0;
    for (s in (nothing of ObjectArray<String>)) {
        sum += s.hash;
    }
    for (s in (nothing of ObjectArray<JString>)) {
        sum += s.hash;
    }
    for (s in (nothing of BooleanArray)) {
        sum += s.hash;
    }
    for (s in (nothing of ByteArray)) {
        sum += s.hash;
    }
    for (s in (nothing of ShortArray)) {
        sum += s.hash;
    }
    for (s in (nothing of IntArray)) {
        sum += s.hash;
    }
    for (s in (nothing of LongArray)) {
        sum += s.hash;
    }
    for (s in (nothing of FloatArray)) {
        sum += s.hash;
    }
    for (s in (nothing of DoubleArray)) {
        sum += s.hash;
    }
    for (s in (nothing of CharArray)) {
        sum += s.hash;
    }
}