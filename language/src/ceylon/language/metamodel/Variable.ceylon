shared interface Variable<Type> 
        satisfies Attribute<Type> {
    
    shared formal void set(Type newValue);
}
