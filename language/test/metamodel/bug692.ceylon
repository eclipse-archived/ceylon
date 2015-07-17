import ceylon.language.meta { type }
import ceylon.language.meta.model {...}

class Bug692 {
    shared new () {}
    shared new noArg() {}
    shared new oneArg(String s) {}
}

@test
shared void bug692() {
    Function<Bug692,[String]> constructor = `Bug692.oneArg`;
    
    print(type(constructor));
    // and
    print((constructor of Anything)
        is Constructor<Bug692,[String]>);
}
