variable String bug1739CallSequence = "";

void bug1739CallTwice(Anything() callable) {
    callable();
    callable();
}

class Bug1739A() {
    bug1739CallSequence += "A";
    shared String s() {
        bug1739CallSequence += "s";
        return "s";
    }
}
class Bug1739B() {
    bug1739CallSequence += "B";
    shared Bug1739A a {
        bug1739CallSequence += "a";
        return Bug1739A();
    }
}
class Bug1739C() {
    bug1739CallSequence += "C";
    shared Bug1739B b {
        bug1739CallSequence += "b";
        return Bug1739B();
    }
}
class Bug1739D() {
    bug1739CallSequence += "D";
    shared Bug1739C c {
        bug1739CallSequence += "c";
        return Bug1739C();
    }
}

shared void bug1739() {
    bug1739CallTwice(Bug1739D().c.b.a.s);
    assert("DcCbBaAss"==bug1739CallSequence);
}