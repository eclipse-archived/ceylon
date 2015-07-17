import ceylon.language.meta.model {...}

class Bug694 {
    shared new () {}
    shared new noArg() {}
    shared new oneArg(String s) {}
}

@test
shared void bug694() {
    try{
        assert(is Function<Bug694, [Float]> k =`Bug694`.getConstructor("oneArg"));
    }catch(Exception x){
        assert(is IncompatibleTypeException x);
    }
}
