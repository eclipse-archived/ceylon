import ceylon.language.meta.model {...}

class Bug693 {
    shared new () {}
    shared new NoArg() {}
    shared new OneArg(String s) {}
}

@test
shared void bug693() {
    `Bug694`.declaration.annotatedConstructorDeclarations<SharedAnnotation>();
}
