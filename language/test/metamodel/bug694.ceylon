import ceylon.language.meta.model {...}

class Bug694 {
    shared new () {}
    shared new NoArg() {}
    shared new OneArg(String s) {}
}

@test
shared void bug694() {
    try{
        `Bug694`.getConstructor<[Float]>("OneArg");
        assert(false);
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }
}
