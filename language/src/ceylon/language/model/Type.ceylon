shared interface Type<out Type=Anything> /* of ClassOrInterface
                                 | UnionType
                                 | IntersectionType
                                 | nothingType */ {
    
    shared formal Boolean isTypeOf(Anything instance);
    // add isSuperTypeOf(Type)
    // add isSubTypeOf(Type) => type.isSuperTypeOf(this)
    // add isExactlyTypeOf(Type)
}
