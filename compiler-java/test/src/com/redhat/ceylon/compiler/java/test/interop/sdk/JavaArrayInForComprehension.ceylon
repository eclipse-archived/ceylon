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

alias Ints=>IntArray;
alias Strings=>ObjectArray<String>;
alias JStrings=>ObjectArray<JString>;


@noanno
void javaArrayInForComprehension() {
    value yesNo = [true, false];
    value abc = ["a", "b", "c"];
    value jabc = [JString("a"), JString("b"), JString("c")];
    value _123 = [1, 2, 3];
    value cafebabe = [#ca.byte, #fe.byte, #ba.byte, #be.byte];
    value xyz = ['x', 'y', 'z'];
    value _789 = [7.0, 8.0, 9.0];
    
    assert(abc == {for (s in (toArray<ObjectArray<String>,String>(abc))) s}.sequence());
    assert(jabc == {for (s in (toArray<ObjectArray<JString>,JString>(jabc))) s}.sequence());
    assert(yesNo == {for (s in (toArray<BooleanArray,Boolean>(yesNo))) s}.sequence());
    assert(cafebabe == {for (s in (toArray<ByteArray,Byte>(cafebabe))) s}.sequence());
    assert(_123 == {for (s in (toArray<ShortArray,Integer>(_123))) s}.sequence());
    assert(_123 == {for (s in (toArray<IntArray,Integer>(_123))) s}.sequence());
    assert(_123 == {for (s in (toArray<LongArray,Integer>(_123))) s}.sequence());
    
    assert(_789 == {for (s in (toArray<FloatArray,Float>(_789))) s}.sequence());
    assert(_789 == {for (s in (toArray<DoubleArray,Float>(_789))) s}.sequence());
    assert(xyz == {for (s in (toArray<CharArray,Character>(xyz))) s}.sequence());
    
    // via a type alias
    assert(_123 == {for (s in (toArray<IntArray,Integer>(_123) of Ints)) s}.sequence());
    assert(abc == {for (s in (toArray<ObjectArray<String>,String>(abc) of Strings)) s}.sequence());
    assert(jabc == {for (s in (toArray<ObjectArray<JString>,JString>(jabc) of JStrings)) s}.sequence());
    
    // with an `if`
    assert([2] == {for (x in (toArray<IntArray,Integer>(_123))) if (x%2 == 0) x}.sequence());
    assert([1,3] == {for (x in (toArray<IntArray,Integer>(_123))) if (x%2 == 1) x}.sequence());
    
    // non-initial `for`s
    assert([1,2,3,2,4,6,3,6,9] == {for (x in (toArray<IntArray,Integer>(_123))) for (y in (toArray<IntArray,Integer>(_123))) x*y}.sequence());
    
}