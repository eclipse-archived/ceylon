shared interface Variable<Type> 
        satisfies Value<Type> {
    
    shared formal void set(Type newValue);
}
