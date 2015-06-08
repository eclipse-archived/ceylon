import ceylon.language.meta { type }
import ceylon.language.meta.model {...}

class Bug692 {
    shared new () {}
    shared new NoArg() {}
    shared new OneArg(String s) {}
}

@test
shared void bug692() {
    Constructor<Bug692,[String]> constructor = `Bug692.OneArg`;
    
    print(type(constructor));
    // and
    print((constructor of Anything)
        is Constructor<Bug692,[String]>);
}
