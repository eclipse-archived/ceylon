shared interface ParameterisedType<out DeclarationType>
    satisfies Type
    given DeclarationType satisfies ClassOrInterface {
    
    shared formal DeclarationType declaration;
    
    shared formal ParameterisedType<Class>? superclass;
    
    shared formal ParameterisedType<Interface>[] interfaces;

    shared formal Map<TypeParameter, Type> typeArguments;
}
