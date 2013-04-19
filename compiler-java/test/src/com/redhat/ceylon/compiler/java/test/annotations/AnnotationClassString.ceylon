import ceylon.language.metamodel{SequencedAnnotation, Type}

@noanno
annotation class AnnotationClassString(String s) satisfies SequencedAnnotation<AnnotationClassString, Type<Anything>>{}
@noanno
annotation class AnnotationClassStringDefaulted(String s="foo") satisfies SequencedAnnotation<AnnotationClassString, Type<Anything>>{}
@noanno
annotation class AnnotationClassStringSequence(String[] s) {}
@noanno
annotation class AnnotationClassStringSequenceDefaulted(String[] s=["foo"]) {}
@noanno
annotation class AnnotationClassStringIterable({String*} s) {}
@noanno
annotation class AnnotationClassStringIterableDefaulted({String*} s={"foo"}) {}
@noanno
annotation class AnnotationClassStringVariadic({String*} s) {}
@noanno
annotation AnnotationClassString annotationClassString(String s) { return AnnotationClassString(s); }
@noanno
annotation AnnotationClassString annotationClassStringStatic() => AnnotationClassString("bar");
@noanno
annotation AnnotationClassString annotationClassStringDropped(String s, String t) => AnnotationClassString(s);
@noanno
annotation AnnotationClassString annotationClassStringDropped2(String t, String s) => AnnotationClassString(s);
@noanno
annotation AnnotationClassStringDefaulted annotationClassStringDefaulted() => AnnotationClassStringDefaulted();
@noanno
annotationClassString("baz")
annotationClassStringStatic()
annotationClassStringDropped("yes", "no")
annotationClassStringDropped2("no", "yes")
annotationClassStringDefaulted()
class AnnotationClassString_callsite() {}
