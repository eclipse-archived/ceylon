@noanno
shared class Bug6997FirstClass {
    shared new () { }
}

@noanno
shared class Bug6997SecondClass extends Bug6997FirstClass {
    shared new () extends Bug6997FirstClass() { }
    shared new secondConstructor() extends Bug6997SecondClass() { }
}
