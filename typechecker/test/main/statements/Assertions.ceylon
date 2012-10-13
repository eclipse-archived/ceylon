void assertions() {
    Void name = "gavin";
    doc "name is required" 
    assert (exists o = name);
    assert (o=="gavin");
    doc "name must be a string"
    assert (is String n = name);
    print(n.uppercased);
    assert (is String name);
    assert (exists arg = process.arguments[0], 
            arg=="gavin");
    @error assert ();
    print(Assertions("hello").name);
    Void x = null;
    assert (is Object x, x=="hello");
    print(x=="goodbye");
    Void y = null;
    if (is Object y, y=="hello") {
        print(y=="goodbye");
    }
    @error print(y=="goodbye");
}

class Assertions(String? nameOrNull) {
    assert (exists nameOrNull);
    shared String name = nameOrNull;
    shared actual String string {
        return name;
    }
}
