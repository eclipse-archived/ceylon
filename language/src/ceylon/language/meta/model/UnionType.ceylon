"A closed union type."
shared interface UnionType<out Union=Anything>
        satisfies Type<Union> {
    
    "The list of closed case types of this union."
    shared formal List<Type<Union>> caseTypes;
}
