import ceylon.language.meta.declaration { ClassOrInterfaceDeclaration }

final annotation class RecursiveConstructors()
        satisfies SequencedAnnotation<RecursiveConstructors, ClassOrInterfaceDeclaration>{
}

@error
annotation RecursiveConstructors recursiveConstructors1() => recursiveConstructors1();
recursiveConstructors1
class RecursiveConstructors_callsite1() {}
@error
annotation RecursiveConstructors recursiveConstructors2() => recursiveConstructors3();
@error
annotation RecursiveConstructors recursiveConstructors3() => recursiveConstructors2();
recursiveConstructors2
class RecursiveConstructors_callsite2() {}