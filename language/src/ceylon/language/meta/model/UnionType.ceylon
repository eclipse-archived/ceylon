"A closed union type."
shared sealed interface UnionType<out Union>
        satisfies Type<Union> {
    
    "The list of closed case types of this union."
    shared formal List<Type<>> caseTypes;
}
