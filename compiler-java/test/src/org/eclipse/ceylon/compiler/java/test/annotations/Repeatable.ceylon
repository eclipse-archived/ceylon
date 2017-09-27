import ceylon.language.meta.declaration{ClassDeclaration}
import ceylon.language.meta {
    sequencedAnnotations
}

final annotation class Repeatable()
        satisfies SequencedAnnotation<Repeatable, ClassDeclaration> {}

annotation Repeatable repeatable() => Repeatable();

repeatable
repeatable
class RepeatableUse() {
    print(sequencedAnnotations(`Repeatable`, `class RepeatableUse`));
    assert(sequencedAnnotations(`Repeatable`, `class RepeatableUse`).size == 2);
}
