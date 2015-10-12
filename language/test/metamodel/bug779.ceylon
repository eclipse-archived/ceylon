import ceylon.language.meta.declaration {
    ClassDeclaration,
    FunctionDeclaration
}
import ceylon.language.meta {
    type
}

shared class Bug779A {
    shared new() {}    
    shared void b() {}
}

@test
shared void bug779(){
    [FunctionDeclaration, ClassDeclaration?] cd = [`function Bug779A.b`, `class Bug779A`];
    switch(cd)
    case(is [FunctionDeclaration, ClassDeclaration?]) {
    }
}