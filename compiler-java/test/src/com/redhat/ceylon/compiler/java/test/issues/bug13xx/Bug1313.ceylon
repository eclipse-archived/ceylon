class Bug1313() satisfies Summable<Bug1313>{
    shared actual Bug1313 plus(Bug1313 other) => nothing;
}

void bug1313() {
    // Value types with boxed parameter (refining plus)
    value methodReference = 1.plus;
    value methodReference2 = "".plus;
    // Value types with unboxed parameter (replace is not actual)
    value methodReference3 = "".replace;
    // Non value type with boxed parameter (refining plus)
    value methodReference4 = Bug1313().plus;
}