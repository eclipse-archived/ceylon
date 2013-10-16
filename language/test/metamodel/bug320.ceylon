import ceylon.language.meta { type }

@test
shared void bug320() {
    void x(){}
    print(type(x));
}