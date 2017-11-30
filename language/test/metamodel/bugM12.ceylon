import ceylon.language.meta.declaration {
    ClassDeclaration,
    FunctionDeclaration
}
import ceylon.language.meta {
    type
}

shared class BugM12A {
    shared new() {}    
    shared void b() {}
}

@test
shared void bugM12test(){
    [FunctionDeclaration, ClassDeclaration?] cd = [`function BugM12A.b`, `class BugM12A`];
    type(cd);
}