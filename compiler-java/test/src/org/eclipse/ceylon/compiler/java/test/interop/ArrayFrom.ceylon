import ceylon.language.meta.model{Method}
import java.lang{
    ShortArray,
    IntArray,
    LongArray,
    ByteArray,
    CharArray,
    BooleanArray,
    FloatArray,
    DoubleArray,
    ObjectArray,
    JInteger=Integer,
    JCharacter=Character,
    JLong=Long,
    JShort=Short,
    JByte=Byte,
    JBoolean=Boolean,
    JFloat=Float,
    JDouble=Double,
    JString=String
}
import java.util{Arrays}
@noanno
alias JavaArray<T> =>
        ShortArray|
        IntArray|
        LongArray|
        ByteArray|
        CharArray|
        BooleanArray|
        FloatArray|
        DoubleArray|
        ObjectArray<T>;

@noanno
class ArrayFrom() {
    
    void check<E>(List<E> expected, JavaArray<E> array) {
        switch (array)
        case (is LongArray) {
            assert(is List<Integer> expected);
            assert(Arrays.equals(LongArray.with(expected), array));
        } case (is IntArray) {
            assert(is List<Integer> expected);
            assert(Arrays.equals(IntArray.with(expected), array));
        } case (is ShortArray) {
            assert(is List<Integer> expected);
            assert(Arrays.equals(ShortArray.with(expected), array));
        } case (is ByteArray) {
            assert(is List<Byte> expected);
            assert(Arrays.equals(ByteArray.with(expected), array));
        } case (is BooleanArray) {
            assert(is List<Boolean> expected);
            assert(Arrays.equals(BooleanArray.with(expected), array));
        } case (is CharArray) {
            assert(is List<Character> expected);
            assert(Arrays.equals(CharArray.with(expected), array));
        } case (is FloatArray) {
            assert(is List<Float> expected);
            assert(Arrays.equals(FloatArray.with(expected), array));
        } case (is DoubleArray) {
            assert(is List<Float> expected);
            assert(Arrays.equals(DoubleArray.with(expected), array));
        } case (is ObjectArray<Anything>) {
            value s = array.iterable.sequence();
            assert(expected == s);
        } 
    }
    
    shared void positional() {
        Array<Integer> a0 = Array{1, 2, 3};
        Array<JLong> a1 = Array{JLong(1), JLong(2), JLong(3)};
        variable LongArray longArray = LongArray.from(a0);
        check(1..3, longArray);
        longArray = LongArray.from(a1);
        check(1..3, longArray);
        
        Array<JInteger> a2 = Array{JInteger(97), JInteger(98), JInteger(99)};
        Array<Character> a3 = Array{'a', 'b', 'c'};
        variable IntArray intArray = IntArray.from(a2);
        check(97..99, intArray);
        intArray = IntArray.from(a3);
        check(97..99, intArray);
        
        Array<JShort> a4 = Array{JShort(1), JShort(2), JShort(3)};
        ShortArray shortArray = ShortArray.from(a4);
        check(1..3, shortArray);
        
        Array<Byte> a5 = Array{1.byte, 2.byte, 3.byte};
        Array<JByte> a6 = Array{JByte(1.byte), JByte(2.byte), JByte(3.byte)};
        variable ByteArray byteArray = ByteArray.from(a5);
        check(1.byte..3.byte, byteArray);
        byteArray = ByteArray.from(a6);
        check(1.byte..3.byte, byteArray);
        
        Array<Boolean> a7 = Array{true, false, true};
        Array<JBoolean> a8 = Array{JBoolean(true), JBoolean(false), JBoolean(true)};
        variable BooleanArray booleanArray = BooleanArray.from(a7);
        check([true, false, true], booleanArray);
        booleanArray = BooleanArray.from(a8);
        check([true, false, true], booleanArray);
        
        Array<JFloat> a9 = Array{JFloat(1.0), JFloat(2.0), JFloat(3.0)};
        FloatArray floatArray = FloatArray.from(a9);
        check([1.0, 2.0, 3.0], floatArray);
        
        Array<Float> a10 = Array{1.0, 2.0, 3.0};
        Array<JDouble> a11 = Array{JDouble(1.0), JDouble(2.0), JDouble(3.0)};
        variable DoubleArray doubleArray = DoubleArray.from(a10);
        check([1.0, 2.0, 3.0], doubleArray);
        doubleArray = DoubleArray.from(a11);
        check([1.0, 2.0, 3.0], doubleArray);
        
        Array<JCharacter> a12 = Array{JCharacter('a'), JCharacter('b'), JCharacter('c')};
        CharArray charArray = CharArray.from(a12);
        check('a'..'c', charArray);
        
        ObjectArray<Object> objArray = ObjectArray<Object>.from(Array<Object?>{true, "", 1});
        check([true, "", 1], objArray);
        ObjectArray<Integer> integerArray = ObjectArray<Integer>.from(Array<Integer?>{1, 2, 3});
        check(1..3, integerArray);
        ObjectArray<JLong> jlongArray = ObjectArray<JLong>.from(Array<JLong?>{JLong(1), JLong(2), JLong(3)});
        check([JLong(1), JLong(2), JLong(3)], jlongArray);
        ObjectArray<Object> jlongArray2 = ObjectArray<Object>.from(Array<Object?>{JLong(1), JLong(2), JLong(3)});
        check([JLong(1), JLong(2), JLong(3)], jlongArray);
        ObjectArray<String> stringArray = ObjectArray<String>.from(Array<String?>{"foo", "bar", "baz"});
        check(["foo", "bar", "baz"], stringArray);
        ObjectArray<JString> jStringArray = ObjectArray<JString>.from(Array<JString?>{JString("foo"), JString("bar"), JString("baz")});
        check([JString("foo"), JString("bar"), JString("baz")], jStringArray);
        
        ObjectArray<String> stringArray2 = ObjectArray<String>.from(Array<String?>{"foo", "bar", "baz"});
        assert(stringArray2.get(0)=="foo");
        ObjectArray<JString> jStringArray2 = ObjectArray<JString>.from(Array<JString?>{JString("foo"), JString("bar"), JString("baz")});
        assert(jStringArray2.get(0)==JString("foo"));
    }
    
    shared void namedArgs() {
        LongArray emptyLongArray = LongArray.from{array=Array<Integer>{};};
        LongArray longArray = LongArray.from{array=Array{1, 2, 3};};
        LongArray longArray2 = LongArray.from{array=Array<JLong>{JLong(1), JLong(2), JLong(3)};};
        
        IntArray intArray = IntArray.from{array=Array<JInteger>{JInteger(1), JInteger(2), JInteger(3)};};
        IntArray intArray2 = IntArray.from{array=Array<Character>{'a','b','c'};};
        
        ShortArray shortArray = ShortArray.from{array=Array<JShort>{JShort(1), JShort(2), JShort(3)};};
        
        ByteArray byteArray = ByteArray.from{array=Array{1.byte, 2.byte, 3.byte};};
        ByteArray byteArray2 = ByteArray.from{array=Array<JByte>{JByte(1.byte), JByte(2.byte), JByte(3.byte)};};
        
        BooleanArray booleanArray = BooleanArray.from{array=Array{true, false, true};};
        BooleanArray booleanArray2 = BooleanArray.from{array=Array<JBoolean>{JBoolean(true), JBoolean(false), JBoolean(true)};};
        
        FloatArray floatArray = FloatArray.from{array=Array<JFloat>{JFloat(1.0), JFloat(2.0), JFloat(3.0)};};
        
        DoubleArray doubleArray = DoubleArray.from{array=Array{1.0, 2.0, 3.0};};
        DoubleArray doubleArray2 = DoubleArray.from{array=Array<JDouble>{JDouble(1.0), JDouble(2.0), JDouble(3.0)};};
        
        CharArray charArray = CharArray.from{array=Array<JCharacter>{JCharacter('a'), JCharacter('b'), JCharacter('c')};};
        
        ObjectArray<Object> objArray = ObjectArray<Object>.from{array=Array<Object?>{true, "", 1};};
        ObjectArray<Integer> integerArray = ObjectArray<Integer>.from{array=Array<Integer?>{1, 2, 3};};
    }
    
    shared void ref() {
        LongArray(Array<Integer>) longRef = LongArray.from;
        check(1..3, longRef(Array<Integer>{1,2,3}));
        
        IntArray(Array<JInteger>|Array<Character>) intRef = IntArray.from;
        intRef(Array<JInteger>{JInteger(1),JInteger(2),JInteger(3)});
        intRef(Array<Character>{'a','b','c'});
        
        ObjectArray<Object>(Array<Object?>) objRef = ObjectArray<Object>.from;
        ObjectArray<Object> objArray = objRef(Array<Object?>{true, "", 1});
        
        ObjectArray<Integer>(Array<Integer?>) objRef2 = ObjectArray<Integer>.from;
        ObjectArray<Integer> objArray2 = objRef2(Array<Integer?>{1,2,3});
    }
    
    shared void meta() {
        // TODO fix this when we've implemented the metamodel for statics
        Method<Null,LongArray,[Array<JLong>|Array<Integer>]> longRef = `LongArray.from`;
        check(1..3, longRef(null)(Array<Integer>{1,2,3}));
        check(1..3, longRef(null)(Array<JLong>{JLong(1), JLong(2), JLong(3)}));
        
        value intRef = `IntArray.from`;
        check(1..3, intRef(null)(Array<JInteger>{JInteger(1),JInteger(2),JInteger(3)}));
        check([97,98,99], intRef(null)(Array<Character>{'a','b','c'}));
        
        value shortRef = `ShortArray.from`;
        check(1..3, shortRef(null)(Array<JShort>{JShort(1),JShort(2),JShort(3)}));
        
        Method<Null,DoubleArray,[Array<JDouble>|Array<Float>]> doubleRef = `DoubleArray.from`;
        check([1.0,2.0,3.0], doubleRef(null)(Array<Float>{1.0,2.0,3.0}));
        check([1.0,2.0,3.0], doubleRef(null)(Array<JDouble>{JDouble(1.0), JDouble(2.0), JDouble(3.0)}));
        
        value floatRef = `FloatArray.from`;
        check([1.0,2.0,3.0], floatRef(null)(Array<JFloat>{JFloat(1.0),JFloat(2.0),JFloat(3.0)}));
        
        
        value booleanRef = `BooleanArray.from`;
        check([true,false,true], booleanRef(null)(Array<JBoolean>{JBoolean(true),JBoolean(false),JBoolean(true)}));
        check([true,false,true], booleanRef(null)(Array<Boolean>{true,false,true}));
        
        value byteRef = `ByteArray.from`;
        check([1.byte,2.byte,3.byte], byteRef(null)(Array<JByte>{JByte(1.byte),JByte(2.byte),JByte(3.byte)}));
        check([1.byte,2.byte,3.byte], byteRef(null)(Array<Byte>{1.byte,2.byte,3.byte}));
        
        value charRef = `CharArray.from`;
        check(['a','b','c'], charRef(null)(Array<JCharacter>{JCharacter('a'),JCharacter('b'),JCharacter('c')}));
        
        value objRef = `ObjectArray<Integer>.from`;
        check(1..3, objRef(null)(Array<Integer?>{1,2,3}));
    }
    
}

void arrayFrom() {
    value aw = ArrayFrom();
    aw.positional();
    aw.namedArgs();
    aw.ref();
    aw.meta();
}