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
shared sealed
interface Type<out Target=Anything> /* of ClassOrInterface
                                        | UnionType
                                        | IntersectionType
                                        | nothingType */ {
    
    "True if the given instance is of this type, or is of a subtype of this type."
    shared formal Boolean typeOf(Anything instance);
    
    "True if this type is a supertype of the given type, or if both types are the same."
    shared formal Boolean supertypeOf(AppliedType<> type);
    
    "True if this type is a subtype of the given type, or if both types are the same."
    shared default Boolean subtypeOf(AppliedType<> type) => type.supertypeOf(this);
    
    "True if the given type is a exactly this type."
    shared formal Boolean exactly(AppliedType<> type);
    
    "The union of this type with the other type."
    shared formal Type<Target|Other> union<Other>(Type<Other> other);
    
    "The intersection of this type with the other type."
    shared formal Type<Target&Other> intersection<Other>(Type<Other> other);
}
