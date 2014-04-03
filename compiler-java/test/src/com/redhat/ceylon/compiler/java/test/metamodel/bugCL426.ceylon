import ceylon.language.meta.declaration { FunctionDeclaration }

final annotation class MyAnnotation() satisfies OptionalAnnotation<MyAnnotation>{}

@noanno
void bugCL426() {
    C1();
    value c1Decl = `class C1`;
    c1Decl.annotatedMemberDeclarations<FunctionDeclaration, MyAnnotation>();
}
