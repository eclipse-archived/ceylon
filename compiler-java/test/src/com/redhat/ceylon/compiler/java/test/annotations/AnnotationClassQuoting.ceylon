import ceylon.language.metamodel{SequencedAnnotation}
import ceylon.language.metamodel.declaration { ClassOrInterfaceDeclaration }

@nomodel
annotation class AnnotationClassQuoting(
    shared actual String string,
    shared String toString,
    shared actual Integer hash,
    shared Integer hashCode) 
    satisfies SequencedAnnotation<AnnotationClassQuoting, ClassOrInterfaceDeclaration>
        & Cloneable<AnnotationClassQuoting> {

    shared actual AnnotationClassQuoting clone => nothing;
}

@nomodel
annotation AnnotationClassQuoting annotationClassQuoting() => AnnotationClassQuoting("", "", -1, 0);
@nomodel
annotation AnnotationClassQuoting annotationClassQuoting2(
String string,
String toString,
Integer hash,
Integer hashCode) => AnnotationClassQuoting(string, toString, hash, hashCode);
@nomodel
annotationClassQuoting
annotationClassQuoting2("", "", -2, 0)
class AnnotationClassQuoting_callsite() {}