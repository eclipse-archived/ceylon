import ceylon.language.meta.declaration { ... }

shared final annotation
class Bug2045Annotation(Integer order)
        satisfies OptionalAnnotation<Bug2045Annotation, ValueDeclaration>{}

shared annotation
Bug2045Annotation bug2045(Integer order = -1) => Bug2045Annotation(order);

bug2045
String bug2045Use = "";