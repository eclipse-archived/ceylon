shared interface Variable<Type> 
        satisfies Value<Type> {

    // FIXME: refine declaration type to be VariableDeclaration?
    
    shared formal void set(Type newValue);
}
