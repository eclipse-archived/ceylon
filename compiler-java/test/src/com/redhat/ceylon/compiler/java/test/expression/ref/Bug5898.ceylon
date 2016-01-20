class Bug5898() {
    shared class D {
        shared new () {}
        shared new create() {}
    }
}
void bug5898() {
    Bug5898 c = nothing;//Bug5898();
    //value ref = Bug5898.D.create;
    Bug5898.D d = (Bug5898.D.create)(c)();
    //Bug5898.D d = Bug5898.D(c)();
    //Bug5898.D d = Bug5898.D.create(c)();
}