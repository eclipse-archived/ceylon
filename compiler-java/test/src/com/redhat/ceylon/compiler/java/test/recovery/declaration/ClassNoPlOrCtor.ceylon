shared class ClassNoPlOrCtor {
}
shared void classNoPlOrCtor(Anything(Integer) print) {
    ClassNoPlOrCtor? ignored = null;
    print(1);
    //value ref = ClassNoPlOrCtor;
}