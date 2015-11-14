import java.lang{IntArray}

Integer[] optimizedTupleEnumWithComprehensionOverTuple() {
    [Integer*] xs = [0,1,2,3,4,5,6,7,8,9];
    return [ for (x in xs) x ];
}
Integer[] optimizedTupleEnumWithComprehensionOverArraySequence() {
    Integer[] xs = {0,1,2,3,4,5,6,7,8,9}.sequence;
    return [ for (x in xs) x ];
}
Integer[] optimizedTupleEnumWithComprehensionOverRange() {
    Range<Integer> xs = 0..9;
    return [ for (x in xs) x ];
}
Integer[] optimizedTupleEnumWithComprehensionOverString() {
    String xs = "0123456789";
    return [ for (x in xs) x.integer - '0'.integer ];
}
Integer[] optimizedTupleEnumWithComprehensionOverArray() {
    IntArray xs = IntArray(10);
    return [ for (x in xs.array) x ];
}
