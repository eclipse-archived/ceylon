shared interface UnionType<out Union> satisfies Type<Union> {
    shared formal List<Type<Union>> caseTypes;
}
