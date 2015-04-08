import ceylon.language.meta{
    type, 
    annotations, optionalAnnotation, sequencedAnnotations
}


see(`new Constructor`) 
class CompilerBug2116Class {
    shared new Constructor() {}
}

@test
shared void compilerBug2116() {
    assert(exists classexpect = `class CompilerBug2116Class`.getConstructorDeclaration("Constructor"),
        exists classsee = annotations(`SeeAnnotation`, `class CompilerBug2116Class`).first,
        exists classfirst = classsee.programElements.first,
        classexpect==classfirst);
}