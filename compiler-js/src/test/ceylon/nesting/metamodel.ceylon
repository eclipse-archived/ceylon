import ceylon.language.meta.declaration {
    ClassOrInterfaceDeclaration,
    FunctionDeclaration,
    ValueDeclaration
}

shared final annotation class Test489Annotation()
    satisfies OptionalAnnotation<Test489Annotation,
        ClassOrInterfaceDeclaration|FunctionDeclaration|ValueDeclaration>{}
shared annotation Test489Annotation test489()
    => Test489Annotation();

//Metamodel tests (model2)
shared object sharedObject {
  string="shared object in main package";
}
shared test489 Integer sharedValue = 1;

test489 object unsharedObject {
  string="unshared object in main package";
}
test489 Integer unsharedValue = 2;

shared void sharedFun() {}
test489 void unsharedFun() {}
