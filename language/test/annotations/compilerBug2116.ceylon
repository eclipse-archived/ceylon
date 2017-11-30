import ceylon.language.meta{
    type, 
    annotations, optionalAnnotation, sequencedAnnotations
}


see(`new constructor`) 
class CompilerBug2116Class {
    shared new constructor() {}
}

@test
shared void compilerBug2116() {
    assert(exists classexpect = `class CompilerBug2116Class`.getConstructorDeclaration("constructor"),
        exists classsee = annotations(`SeeAnnotation`, `class CompilerBug2116Class`).first,
        exists classfirst = classsee.programElements.first,
        classexpect==classfirst);
}