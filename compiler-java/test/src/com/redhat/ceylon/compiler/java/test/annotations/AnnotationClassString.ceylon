import ceylon.language.meta.declaration { ClassOrInterfaceDeclaration }

@nomodel
annotation final class AnnotationClassString(String s) satisfies SequencedAnnotation<AnnotationClassString, ClassOrInterfaceDeclaration>{}
@nomodel
annotation final class AnnotationClassStringDefaulted(String s="foo") satisfies SequencedAnnotation<AnnotationClassStringDefaulted, ClassOrInterfaceDeclaration>{}
@nomodel
annotation final class AnnotationClassStringSequence(String[] s) satisfies OptionalAnnotation<AnnotationClassStringSequence, ClassOrInterfaceDeclaration> {}
@nomodel
annotation final class AnnotationClassStringSequenceDefaulted(String[] s=["foo"]) satisfies OptionalAnnotation<AnnotationClassStringSequenceDefaulted, ClassOrInterfaceDeclaration> {}
@nomodel
annotation final class AnnotationClassStringIterable({String*} s) satisfies OptionalAnnotation<AnnotationClassStringIterable, ClassOrInterfaceDeclaration> {}
@nomodel
annotation final class AnnotationClassStringIterableDefaulted({String*} s={"foo"}) satisfies OptionalAnnotation<AnnotationClassStringIterableDefaulted, ClassOrInterfaceDeclaration> {}
@nomodel
annotation final class AnnotationClassStringVariadic({String*} s) satisfies OptionalAnnotation<AnnotationClassStringVariadic, ClassOrInterfaceDeclaration> {}
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
