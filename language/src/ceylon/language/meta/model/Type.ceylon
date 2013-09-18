import ceylon.language.meta.model { AppliedType = Type }

"A closed type.
 
 A closed type is a type which is fully resolved and bound and contains no open type variables.
 All instance types are closed at runtime.
 
 You have only four sorts of types:
 
 - [[ClassOrInterface]]
 - [[UnionType]]
 - [[IntersectionType]]
 - [[nothingType]]
"
shared interface Type<out Type=Anything> /* of ClassOrInterface
                                 | UnionType
                                 | IntersectionType
                                 | nothingType */ {
    
    "True if the given instance is of this type, or is of a subtype of this type."
    shared formal Boolean isTypeOf(Anything instance);

    "True if the given type is a supertype of this type."
    shared formal Boolean isSuperTypeOf(AppliedType<Anything> type);
    
    "True if the given type is a subtype of this type."
    shared default Boolean isSubTypeOf(AppliedType<Anything> type) => type.isSuperTypeOf(this);

    "True if the given type is a exactly this type."
    shared formal Boolean isExactly(AppliedType<Anything> type);
}
