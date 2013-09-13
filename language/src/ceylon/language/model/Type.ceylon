import ceylon.language.model { AppliedType = Type }

shared interface Type<out Type=Anything> /* of ClassOrInterface
                                 | UnionType
                                 | IntersectionType
                                 | nothingType */ {
    
    shared formal Boolean isTypeOf(Anything instance);
    shared formal Boolean isSuperTypeOf(AppliedType<Anything> type);
    shared default Boolean isSubTypeOf(AppliedType<Anything> type) => type.isSuperTypeOf(this);
    shared formal Boolean isExactly(AppliedType<Anything> type);
}
