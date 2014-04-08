import ceylon.language.meta { type }

@test
shared void bug320() {
    void x(){}
    String s = type(x).string;
}