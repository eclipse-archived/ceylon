import ceylon.language.metamodel{Annotation, Annotated, Type, ConstrainedAnnotation}

@noanno
void bug1206() {
    value s0 = (function () => 2)();
    value s1 = (function () => 2)().string;
    value s2 = (1 == 2 then (function () => 3) else (function () => 4))();

    (bug1206)();
}