import ceylon.language.meta.declaration{FunctionDeclaration}

@noanno
final annotation class AnnotationSpreadArgs(String* args) 
    satisfies SequencedAnnotation<AnnotationSpreadArgs, FunctionDeclaration> {}
@noanno
final annotation class AnnotationSpreadArgs2({String*} args) 
        satisfies SequencedAnnotation<AnnotationSpreadArgs2, FunctionDeclaration> {}
@noanno
annotation AnnotationSpreadArgs annotationSpreadArgs(String* args)
    => AnnotationSpreadArgs(*args);
@noanno
annotation AnnotationSpreadArgs annotationSpreadArgs2({String*} args)
        => AnnotationSpreadArgs(*args);
@noanno
annotation AnnotationSpreadArgs annotationSpreadArgs3([String*] args)
        => AnnotationSpreadArgs{
               args=args;
           };
@noanno
annotation AnnotationSpreadArgs2 annotationSpreadArgs4()
        => AnnotationSpreadArgs2{
    args=["a", "b", "c"];
};
@noanno
annotation AnnotationSpreadArgs2 annotationSpreadArgs5()
        => AnnotationSpreadArgs2(["a", "b", "c"]);
@noanno
annotation AnnotationSpreadArgs2 annotationSpreadArgs6()
        => AnnotationSpreadArgs2{"a", "b", "c"};

@nomodel
annotationSpreadArgs("a", "b", "c")
annotationSpreadArgs{args=["a", "b", "c"];}
annotationSpreadArgs2(["a", "b", "c"])
annotationSpreadArgs2{args=["a", "b", "c"];}
annotationSpreadArgs2{"a", "b", "c"}
annotationSpreadArgs3(["a", "b", "c"])
annotationSpreadArgs3{args=["a", "b", "c"];}
//annotationSpreadArgs3{"a", "b", "c"}
annotationSpreadArgs4
annotationSpreadArgs5
annotationSpreadArgs6
void annotationSpreadArgsUse(){}

// Note: We don't support SpreadArgument within a SequencedArgument