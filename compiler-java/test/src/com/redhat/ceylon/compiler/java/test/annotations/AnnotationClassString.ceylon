import ceylon.language.meta.declaration { ClassOrInterfaceDeclaration }

@nomodel
annotation final class AnnotationClassString(String s) satisfies SequencedAnnotation<AnnotationClassString, ClassOrInterfaceDeclaration>{}
@nomodel
annotation final class AnnotationClassStringDefaulted(String s="foo") satisfies SequencedAnnotation<AnnotationClassStringDefaulted, ClassOrInterfaceDeclaration>{}
@nomodel
annotation final class AnnotationClassStringSequence(String[] s) satisfies Annotation<AnnotationClassStringSequence> {}
@nomodel
annotation final class AnnotationClassStringSequenceDefaulted(String[] s=["foo"]) satisfies Annotation<AnnotationClassStringSequenceDefaulted> {}
@nomodel
annotation final class AnnotationClassStringIterable({String*} s) satisfies Annotation<AnnotationClassStringIterable> {}
@nomodel
annotation final class AnnotationClassStringIterableDefaulted({String*} s={"foo"}) satisfies Annotation<AnnotationClassStringIterableDefaulted> {}
@nomodel
annotation final class AnnotationClassStringVariadic({String*} s) satisfies Annotation<AnnotationClassStringVariadic> {}
@nomodel
annotation AnnotationClassString annotationClassString(String s="s") { return AnnotationClassString(s); }
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
