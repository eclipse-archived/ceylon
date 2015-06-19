import ceylon.language.meta.model {...}

class Bug694 {
    shared new () {}
    shared new noArg() {}
    shared new oneArg(String s) {}
}

@test
shared void bug694() {
    try{
        `Bug694`.getConstructor<[Float]>("oneArg");
        assert(false);
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }
}
