shared interface Interface<out Type> 
        satisfies ClassOrInterface<Type> {
    shared formal actual InterfaceType<Type> apply(ProducedType* types);
}
