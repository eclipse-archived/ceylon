import ceylon.language.meta.declaration {
    FunctionDeclaration,
    Declaration
}

shared final annotation class Bug6207Annotation(shared Declaration d)
        satisfies OptionalAnnotation<Bug6207Annotation, FunctionDeclaration> {}

shared annotation Bug6207Annotation bug6207Annotation(Declaration d)
        => Bug6207Annotation(d);


bug6207Annotation(`package`)
shared void bug6207() {
    `function bug6207`.annotations<Bug6207Annotation>();
}