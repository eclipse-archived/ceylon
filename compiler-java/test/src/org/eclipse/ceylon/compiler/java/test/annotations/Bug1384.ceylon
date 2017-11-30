import ceylon.language.meta.declaration{
    ValueDeclaration
}

@nomodel
shared final annotation class Bug1384(shared ValueDeclaration reference)
        satisfies OptionalAnnotation<Bug1384,ValueDeclaration>{
}
@nomodel
shared final annotation class Bug1384_sequence(shared ValueDeclaration[] references)
        satisfies OptionalAnnotation<Bug1384_sequence,ValueDeclaration>{
}