shared class Bug5899() {
    shared class D {
        shared actual String string;
        shared new create(String s) { string = s; }
    }
    shared object o {
        shared class E {
            shared actual String string;
            shared new create(String s) { string = s; }
        }
    }
}
shared void bug5899() {
    Bug5899.D(String) newd = Bug5899().D.create;
    assert(newd("foo").string == "foo");
    value newe = Bug5899().o.E.create;
    assert(newe("bar").string == "bar");
}