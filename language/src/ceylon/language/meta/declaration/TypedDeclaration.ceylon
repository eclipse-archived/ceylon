"Declaration which has an open type."
shared interface TypedDeclaration {
    
    "The open type for this declaration. For example, the open type for `List<T> f<T>()` is `List<T>`."
    shared formal OpenType openType;
}