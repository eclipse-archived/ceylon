import ceylon.language.meta.declaration{
    ClassWithInitializerDeclaration
}
import ceylon.language.meta{
    type
}

shared class BugC2392() {}

@test
shared void bugC2392() {
    ClassWithInitializerDeclaration ocDec = `class BugC2392`;
    value t = type(ocDec).string;
}