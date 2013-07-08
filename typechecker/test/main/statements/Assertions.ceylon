void assertions() {
    Anything name = "gavin";
    doc ("name is required") 
    assert (exists o = name);
    assert (o=="gavin");
    "name must be a string"
    assert (is String n = name);
    print(n.uppercased);
    assert (is String name);
    assert (exists arg = process.arguments[0], 
            arg=="gavin");
    @error assert ();
    print(Assertions("hello").name);
    Anything x = null;
    assert (is Object x, x=="hello");
    print(x=="goodbye");
    Anything y = null;
    if (is Object y, y=="hello") {
        print(y=="goodbye");
    }
    @error print(y=="goodbye");
    Integer[] ints = 0..10;
    assert (exists int0 = ints[0], is Integer int1 = ints[1]);
    print(int0+1);
    print(int1-1);
}

class Assertions(String? nameOrNull) {
    assert (exists nameOrNull);
    "name \"``nameOrNull``\" must not be empty"
    assert (nameOrNull.size>0);
    shared String name = nameOrNull;
    shared actual String string {
        return name;
    }
}
