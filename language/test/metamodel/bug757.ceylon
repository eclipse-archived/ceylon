import ceylon.language.meta.declaration{...}

class Bug757 {
    shared new x {}
    shared new y() {}
    shared new z(String z) {}
}

@test
shared void bug757() {
    value bc = `class Bug757`;
    variable value ctors = bc.annotatedMemberDeclarations<ConstructorDeclaration, SharedAnnotation>();
    assert(`new Bug757.x` in ctors);
    assert(`new Bug757.y` in ctors);
    assert(`new Bug757.z` in ctors);
    assert(ctors.size == 3);
    ctors = bc.annotatedMemberDeclarations<CallableConstructorDeclaration, SharedAnnotation>();
    assert(`new Bug757.y` in ctors);
    assert(`new Bug757.z` in ctors);
    assert(ctors.size == 2);
    ctors = bc.annotatedMemberDeclarations<ValueConstructorDeclaration, SharedAnnotation>();
    assert(`new Bug757.x` in ctors);
    assert(ctors.size == 1);
    
    value fns = bc.annotatedDeclaredMemberDeclarations<FunctionDeclaration, SharedAnnotation>();
    assert(fns.empty);
    
}
