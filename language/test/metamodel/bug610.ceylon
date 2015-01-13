import ceylon.language.meta { type }

shared interface Bug610Enum of bug610a {}
object bug610a satisfies Bug610Enum {}

shared final annotation class Bug610MyAnnotation(shared Bug610Enum e = bug610a)
        satisfies OptionalAnnotation<Bug610MyAnnotation, Annotated> {}

shared annotation Bug610MyAnnotation bug610myAnnotation() => Bug610MyAnnotation();

object bug610annotateMe {
    bug610myAnnotation
    shared void myMethod() {
        type(this).getMethods<Nothing>(`Bug610MyAnnotation`);
    }
}

@test
shared void bug610() {
    bug610annotateMe.myMethod();
}
