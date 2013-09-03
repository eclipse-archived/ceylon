import ceylon.language.model{SequencedAnnotation, OptionalAnnotation}
import ceylon.language.model.declaration { ClassOrInterfaceDeclaration }

final annotation class RecursiveConstructors()
        satisfies SequencedAnnotation<RecursiveConstructors, ClassOrInterfaceDeclaration>{
}

annotation RecursiveConstructors recursiveConstructors1() => recursiveConstructors1();
recursiveConstructors1
class RecursiveConstructors_callsite1() {}

annotation RecursiveConstructors recursiveConstructors2() => recursiveConstructors3();
annotation RecursiveConstructors recursiveConstructors3() => recursiveConstructors2();
recursiveConstructors2
class RecursiveConstructors_callsite2() {}