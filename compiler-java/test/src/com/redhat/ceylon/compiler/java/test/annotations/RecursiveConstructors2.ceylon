import ceylon.language.meta.declaration{...}

shared final sealed annotation class CyclicAnno(String s) satisfies SequencedAnnotation<CyclicAnno,ValueDeclaration> {} 

// direct recursion via =>
@error
shared annotation CyclicAnno cyclicAnnoDirect(String s) 
        => cyclicAnnoDirect(s);

// mutual recursion via =>
@error 
shared annotation CyclicAnno cyclicAnnoIndirectA(String s) 
        => cyclicAnnoIndirectB(s);
@error
shared annotation CyclicAnno cyclicAnnoIndirectB(String s) 
        => cyclicAnnoIndirectA(s);
// mutual recursion via return
@error
shared annotation CyclicAnno cyclicAnnoIndirectX(String s) {
    return cyclicAnnoIndirectY(s);
}
@error
shared annotation CyclicAnno cyclicAnnoIndirectY(String s) {
    return cyclicAnnoIndirectX(s);
}

// direct recursion via defaulted argument
@error
shared annotation CyclicAnno cyclicAnnoIndirectW(CyclicAnno x=cyclicAnnoIndirectW()) {
    return CyclicAnno("");
}
// mutual recursion via defaulted argument
@error
shared annotation CyclicAnno cyclicAnnoIndirectU(CyclicAnno v=cyclicAnnoIndirectV()) {
    return CyclicAnno("");
}
@error
shared annotation CyclicAnno cyclicAnnoIndirectV(CyclicAnno u=cyclicAnnoIndirectU()) {
    return CyclicAnno("");
}
