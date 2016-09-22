import ceylon.language{AnnotationType = Annotation}

"A program element that can
 be annotated."
see(`interface Annotation`)
since("1.2.0")
shared interface Annotated {
    "true if this element has at least one annotation of the given annotation type."
    shared formal Boolean annotated<Annotation>() 
            given Annotation satisfies AnnotationType;
}