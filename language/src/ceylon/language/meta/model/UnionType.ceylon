shared interface UnionType<out Union=Anything>
        satisfies Type<Union> {
    shared formal List<Type<Union>> caseTypes;
}
