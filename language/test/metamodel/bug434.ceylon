import ceylon.language.meta.declaration { ClassDeclaration }

class Bug434Supertype<T>() { 
    shared T attribute => nothing;
    shared T method() => nothing;
    shared class Inner(T t) {
        shared T attribute => nothing;
    }
    shared interface InnerInterface {
        shared T attribute => nothing;
    }
}

class Bug434Base() extends Bug434Supertype<String>() {}

@test
shared void bug434() {
    `value Bug434Base.attribute`.memberApply<Bug434Base,String>(`Bug434Base`);
    `function Bug434Base.method`.memberApply<Bug434Base,String,[]>(`Bug434Base`);
    value klass = `class Bug434Base.Inner`.memberClassApply<Bug434Base,Bug434Base.Inner,[String]>(`Bug434Base`);
    klass.getAttribute<Bug434Base.Inner, String>("attribute");
    value int = `interface Bug434Base.InnerInterface`.memberApply<Bug434Base,Bug434Base.InnerInterface>(`Bug434Base`);
    int.getAttribute<Bug434Base.InnerInterface, String>("attribute");
}
