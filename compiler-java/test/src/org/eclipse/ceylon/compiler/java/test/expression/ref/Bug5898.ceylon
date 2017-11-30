class Bug5898() {
    shared class D {
        shared actual String string;
        shared new () { string = "new"; }
        shared new create() { string = "create";}
        shared new create2(String s) { string = s;}
    }
}
void bug5898() {
    Bug5898 c = Bug5898();
    //value ref = Bug5898.D.create;
    Bug5898.D d = (Bug5898.D.create)(c)();
    assert(d.string == "create");
    Bug5898.D d2 = (Bug5898.D.create2)(c)("foo");
    assert(d2.string == "foo");
    //Bug5898.D d = Bug5898.D(c)();
    //Bug5898.D d = Bug5898.D.create(c)();
}