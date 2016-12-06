import java.lang{
    ShortArray,
    IntArray,
    LongArray,
    ByteArray,
    CharArray,
    BooleanArray,
    FloatArray,
    DoubleArray,
    ObjectArray
}
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
class ArrayWith() {
    
    void check<E>(List<E> expected, JavaArray<E> array) {
        switch (array)
        case (is LongArray) {
            value s = array.iterable.sequence();
            assert(expected == s);
        } case (is IntArray) {
            value s = array.iterable.sequence();
            assert(expected == s);
        } case (is ShortArray) {
            value s = array.iterable.sequence();
            assert(expected == s);
        } case (is ByteArray) {
            value s = array.iterable.sequence();
            assert(expected == s);
        } case (is BooleanArray) {
            value s = array.iterable.sequence();
            assert(expected == s);
        } case (is CharArray) {
            value s = array.iterable.sequence();
            assert(expected == s);
        } case (is FloatArray) {
            value s = array.iterable.sequence();
            assert(expected == s);
        } case (is DoubleArray) {
            value s = array.iterable.sequence();
            assert(expected == s);
        } case (is ObjectArray<Anything>) {
            value s = array.iterable.sequence();
            assert(expected == s);
        } 
    }
    
    shared void positional() {
        LongArray longArray = LongArray.with({1, 2, 3});
        check(1..3, longArray);
        IntArray intArray = IntArray.with({1, 2, 3});
        check(1..3, intArray);
        ShortArray shortArray = ShortArray.with({1, 2, 3});
        check(1..3, shortArray);
        
        ByteArray byteArray = ByteArray.with({1.byte, 2.byte, 3.byte});
        check({1.byte, 2.byte, 3.byte}.sequence(), byteArray);
        
        BooleanArray booleanArray = BooleanArray.with({true, false, true});
        
        FloatArray floatArray = FloatArray.with({1.0, 2.0, 3.0});
        DoubleArray doubleArray = DoubleArray.with({1.0, 2.0, 3.0});
        
        CharArray charArray = CharArray.with({'a', 'b', 'c'});
        
        ObjectArray<Object> objArray = ObjectArray<Object>.with({true, "", 1});
        ObjectArray<Integer> integerArray = ObjectArray<Integer>.with({1, 2, 3});
    }
    
    shared void enumeratedElements() {
        LongArray emptyLongArray = LongArray.with{};
        LongArray longArray = LongArray.with{1, 2, 3};
        IntArray intArray = IntArray.with{1, 2, 3};
        ShortArray shortArray = ShortArray.with{1, 2, 3};
        
        ByteArray byteArray = ByteArray.with{1.byte, 2.byte, 3.byte};
        
        BooleanArray booleanArray = BooleanArray.with{true, false, true};
        
        FloatArray floatArray = FloatArray.with{1.0, 2.0, 3.0};
        DoubleArray doubleArray = DoubleArray.with{1.0, 2.0, 3.0};
        
        CharArray charArray = CharArray.with{'a', 'b', 'c'};
        
        ObjectArray<Object> objArray = ObjectArray<Object>.with{true, "", 1};
        ObjectArray<Integer> integerArray = ObjectArray<Integer>.with{1, 2, 3};
    }
    
    shared void spread() {
        {Integer*} longs = 2..3;
        LongArray longArray = LongArray.with{1, *longs};
        check(1..3, longArray);
        LongArray longArray2 = LongArray.with{*longs};
        check(2..3, longArray2);

        ObjectArray<Integer> integerArray = ObjectArray<Integer>.with{1, *longs};
        check(1..3, integerArray);
    }
    
    shared void ref() {
        LongArray({Integer*}) longRef = LongArray.with;
        check(1..3, longRef(1..3));
    }
    
    shared void meta() {
        value longRef = `LongArray.with`;
        check(1..3, longRef(1..3));
        
        value shortRef = `ShortArray.with`;
        check(1..3, shortRef(1..3));
        
        value integerRef = `ObjectArray<Integer>.with`;
        integerRef(1..3);
        
        //value orig = `ObjectArray<Integer>`;
        //orig(1);
        //orig(1, 42);
    }
}
void arrayWith() {
    value aw = ArrayWith();
    aw.positional();
    aw.enumeratedElements();
    aw.spread();
    aw.ref();
    aw.meta();
}