import ceylon.language.model{SequencedAnnotation}
import ceylon.language.model.declaration { ClassOrInterfaceDeclaration }

@nomodel
annotation class AnnotationClassString(String s) satisfies SequencedAnnotation<AnnotationClassString, ClassOrInterfaceDeclaration>{}
@nomodel
annotation class AnnotationClassStringDefaulted(String s="foo") satisfies SequencedAnnotation<AnnotationClassString, ClassOrInterfaceDeclaration>{}
@nomodel
annotation class AnnotationClassStringSequence(String[] s) {}
@nomodel
annotation class AnnotationClassStringSequenceDefaulted(String[] s=["foo"]) {}
@nomodel
annotation class AnnotationClassStringIterable({String*} s) {}
@nomodel
annotation class AnnotationClassStringIterableDefaulted({String*} s={"foo"}) {}
@nomodel
annotation class AnnotationClassStringVariadic({String*} s) {}
@nomodel
annotation AnnotationClassString annotationClassString(String s) { return AnnotationClassString(s); }
@nomodel
annotation AnnotationClassString annotationClassStringStatic() => AnnotationClassString("bar");
@nomodel
annotation AnnotationClassString annotationClassStringDropped(String s, String t) => AnnotationClassString(s);
@nomodel
annotation AnnotationClassString annotationClassStringDropped2(String t, String s) => AnnotationClassString(s);
@nomodel
annotation AnnotationClassStringDefaulted annotationClassStringDefaulted() => AnnotationClassStringDefaulted();
@nomodel
annotationClassString("baz")
annotationClassStringStatic()
annotationClassStringDropped("yes", "no")
annotationClassStringDropped2("no", "yes")
annotationClassStringDefaulted()
class AnnotationClassString_callsite() {}
