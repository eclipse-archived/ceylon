import ceylon.language.meta.declaration { FunctionDeclaration }

withOneDefaultParameter
withTwoDefaultParameters  // error: Compiler bug: compiler bug: no result when transforming annotation at unknown
void foo() {
}

shared annotation AnnotationWithOneDefaultParameter withOneDefaultParameter(
    String name = "")
        => AnnotationWithOneDefaultParameter(name);

shared final annotation class AnnotationWithOneDefaultParameter(shared String name)
        satisfies OptionalAnnotation<AnnotationWithOneDefaultParameter, FunctionDeclaration> {}

shared annotation AnnotationWithTwoDefaultParameters withTwoDefaultParameters(
    String name = "",
    String[] list = [])
        => AnnotationWithTwoDefaultParameters(name, list);

shared final annotation class AnnotationWithTwoDefaultParameters(
    shared String name, 
    shared String[] list)
        satisfies OptionalAnnotation<AnnotationWithTwoDefaultParameters, FunctionDeclaration> {}
