class Bug5965() {
    shared class B() {
        shared class C {
            shared actual String string;
            shared new instance {
                this.string = "instance";
            }
        }
    }
}
shared void bug5965() {
    value iFactory = Bug5965.B.C.instance;
    assert (iFactory(Bug5965().B()).string == "instance");
}