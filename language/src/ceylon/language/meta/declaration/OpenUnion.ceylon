"An open union type."
shared sealed interface OpenUnion satisfies OpenType {
    
    "The list of case types for this open union type."
    shared formal List<OpenType> caseTypes;
}
