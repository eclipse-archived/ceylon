import ceylon.language.meta { type }
import ceylon.language.meta.model {...}

class Bug692 {
    shared new () {}
    shared new noArg() {}
    shared new oneArg(String s) {}
}

@test
shared void bug692() {
    CallableConstructor<Bug692,[String]> constructor = `Bug692.oneArg`;
    
    print(type(constructor));
    // and
    assert((constructor of Anything)
        is CallableConstructor<Bug692,[String]>);
}
