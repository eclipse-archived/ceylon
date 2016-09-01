import java.lang {
    JLong = Long
}
import check {
    check
}

@test
shared void testArrays() {
    // contravariant Array.copyTo() types
    value ceylonStringArray = Array<String>((0..1).map(Object.string));
    value ceylonIntegerArray = Array<Integer>(0..3);
    value javaLongArray = Array<JLong>((0..5).map((Integer i) => JLong.valueOf(i)));
    value objectArray = Array<Object>.ofSize(6, finished);
    ceylonStringArray.copyTo(objectArray);
    ceylonIntegerArray.copyTo(objectArray, 2, 2);
    javaLongArray.copyTo(objectArray, 4, 4);
    check(objectArray[0] is String, "box to c.l::String");
    check(objectArray[3] is Integer, "box to c.l::Integer");
    check(objectArray[4] is JLong, "box to j.l.Long");
    check(objectArray == Array { "0", "1", 2, 3, JLong.valueOf(4), JLong.valueOf(5) },
            "correct values");

    // contravariant Array.copyTo() index validation
    try {
        ceylonStringArray.copyTo(objectArray, -1);
        check(false, "copyTo negative source index");
    } catch (AssertionError e) {}
    try {
        ceylonStringArray.copyTo(objectArray, 0, -1);
        check(false, "copyTo negative destination index");
    } catch (AssertionError e) {}
    try {
        ceylonStringArray.copyTo(objectArray, 1, 0, 2);
        check(false, "failed check: copyTo source length");
    } catch (AssertionError e) {}
    try {
        ceylonStringArray.copyTo(objectArray, 0, 5, 2);
        check(false, "failed check: copyTo destination length");
    } catch (AssertionError e) {}
}
