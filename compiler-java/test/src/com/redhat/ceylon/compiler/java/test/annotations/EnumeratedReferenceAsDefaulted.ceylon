import ceylon.language.meta.declaration{FunctionDeclaration}

@nomodel
shared final annotation class EnumeratedReference_X(shared EnumeratedReference_Y y) 
        satisfies SequencedAnnotation<EnumeratedReference_X, FunctionDeclaration> {}

@nomodel
shared final annotation class EnumeratedReference_Y(Comparison c)
        satisfies SequencedAnnotation<EnumeratedReference_Y, FunctionDeclaration> {} 

@nomodel
annotation EnumeratedReference_X enumeratedReference_z(EnumeratedReference_Y y=EnumeratedReference_Y(smaller)) => EnumeratedReference_X(y);
annotation EnumeratedReference_X enumeratedReference_zz() => EnumeratedReference_X(EnumeratedReference_Y(smaller));

@nomodel
enumeratedReference_z
enumeratedReference_zz
shared void enumeratedReference_callsite() {
}