import ceylon.language.meta.declaration {Import,FunctionDeclaration}

bug6871Sequenced
bug6871Sequenced
shared void bug6871() {
    assert(`module`.dependencies.filter(shuffle(Import.annotated<Annotation>)()).size == 1);
    
    //Annotation
    assert(`function bug6871`.annotated<Annotation>());
    assert(!`function bug6871Unannotated`.annotated<Annotation>());
    
    //SequencedAnnotation
    assert(`function bug6871`.annotated<SequencedAnnotation<Nothing,FunctionDeclaration>>());
    assert(!`function bug6871`.annotated<SequencedAnnotation<ThrownExceptionAnnotation,FunctionDeclaration>>());
    assert(`function bug6871`.annotated<SequencedAnnotation<Bug6871Sequenced,FunctionDeclaration>>());
    
    //OptionalAnnotation
    assert(`function bug6871`.annotated<OptionalAnnotation<Nothing,FunctionDeclaration>>());
    assert(!`function bug6871`.annotated<OptionalAnnotation<FormalAnnotation,FunctionDeclaration>>());
    assert(`function bug6871`.annotated<OptionalAnnotation<SharedAnnotation,FunctionDeclaration>>());
    
    //ConstrainedAnnotation
    assert(`function bug6871`.annotated<ConstrainedAnnotation<Nothing,Nothing,FunctionDeclaration>>());
    assert(!`function bug6871`.annotated<ConstrainedAnnotation<ThrownExceptionAnnotation,ThrownExceptionAnnotation[],FunctionDeclaration>>());
    assert(!`function bug6871`.annotated<ConstrainedAnnotation<FormalAnnotation,FormalAnnotation?,FunctionDeclaration>>());
    assert(`function bug6871`.annotated<ConstrainedAnnotation<SharedAnnotation,SharedAnnotation?,FunctionDeclaration>>());
}
void bug6871Unannotated() {}

final annotation class Bug6871Sequenced() satisfies SequencedAnnotation<Bug6871Sequenced,FunctionDeclaration> {}
annotation Bug6871Sequenced bug6871Sequenced() => Bug6871Sequenced();
