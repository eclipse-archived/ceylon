import ceylon.language.meta.declaration {
    ReferenceDeclaration, Package
}

shared class OpenReference(Package p, Object m) extends OpenValue(p,m)
        satisfies ReferenceDeclaration {
}
