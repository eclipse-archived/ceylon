import ceylon.language.meta {
    annotations
}
import ceylon.language.meta.declaration {
    ClassDeclaration,
    FunctionDeclaration
}
import ceylon.language.meta.model {
    Class
}

@noanno
shared final annotation class A()
        satisfies OptionalAnnotation<A,FunctionDeclaration|ClassDeclaration> {
}
@noanno
A? annos = annotations(`A`, nothing);

