import ceylon.language.meta.declaration {
  ValueDeclaration
}

"A reference value declaration that defines state."
shared sealed interface ReferenceDeclaration
        satisfies ValueDeclaration {}
