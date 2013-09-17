"An open type.
 
 An open type is a type which may contain unbound type variables, such as `List<T>`."
shared interface OpenType of OpenClassOrInterfaceType
                       | OpenTypeVariable
                       | OpenUnion
                       | OpenIntersection
                       | nothingType {}
