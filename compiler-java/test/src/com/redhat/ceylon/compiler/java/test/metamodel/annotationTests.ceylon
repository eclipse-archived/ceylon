
import ceylon.language.metamodel{
    type, 
    annotations, optionalAnnotation, sequencedAnnotations,
    OptionalAnnotation, SequencedAnnotation, Annotated, 
    ClassOrInterface, Class, Interface,
    Function, Value, VariableDeclaration=Variable
}
import ceylon.language.metamodel.untyped {
    ValueDeclaration = Value,
    UntypedDeclaration = Declaration,
    Package
}

Package aPackage {
    value aClassType = type(AClass(""));
    assert(is Class<AClass,[String]> aClassType);
    value aClassDecl = aClassType.declaration;
    value pkg = aClassDecl.packageContainer;
    return pkg;
}

ClassOrInterface<T> annotationType<T>(T t) {
    assert(is ClassOrInterface<T> annoType = type(t));
    return annoType;
}

ClassOrInterface<Shared> sharedAnnotation = annotationType(Shared());
ClassOrInterface<Abstract> abstractAnnotation = annotationType(Abstract());
ClassOrInterface<Variable> variableAnnotation = annotationType(Variable());
ClassOrInterface<Doc> docAnnotation = annotationType(Doc(""));
ClassOrInterface<Seq> seqAnnotation = annotationType(Seq(""));

void checkToplevelValueAnnotations() {

    assert(is ValueDeclaration aToplevelValueDecl = aPackage.getAttribute("aToplevelValue"));
    //shared
    assert(annotations(sharedAnnotation, aToplevelValueDecl) exists);
    assert(optionalAnnotation(sharedAnnotation, aToplevelValueDecl) exists);
    // doc
    assert(exists doc = annotations(docAnnotation, aToplevelValueDecl), 
        doc.description == "aToplevelValue");
    assert(exists doc2 = optionalAnnotation(docAnnotation, aToplevelValueDecl), 
        doc2.description == "aToplevelValue");
    // seq
    variable value seqs = annotations(seqAnnotation, aToplevelValueDecl);
    assert(seqs.size == 2);
    assert(exists seq = seqs[0], seq.seq == "aToplevelFunction");
    assert(exists seq2 = seqs[1], seq2.seq == "aToplevelGetterSetter");
    assert(sequencedAnnotations(seqAnnotation, aToplevelValueDecl).size == 2);

}

void checkToplevelGetterSetterAnnotations() {

    assert(is ValueDeclaration aToplevelGetterSetterDecl = aPackage.getAttribute("aToplevelGetterSetter"));
    //shared
    assert(annotations(sharedAnnotation, aToplevelGetterSetterDecl) exists);
    assert(optionalAnnotation(sharedAnnotation, aToplevelGetterSetterDecl) exists);
    // doc
    assert(exists doc = annotations(docAnnotation, aToplevelGetterSetterDecl), 
        doc.description == "aToplevelGetter");
    assert(exists doc2 = optionalAnnotation(docAnnotation, aToplevelGetterSetterDecl), 
        doc2.description == "aToplevelGetter");
    // seq
    variable value seqs = annotations(seqAnnotation, aToplevelGetterSetterDecl);
    assert(seqs.size == 1);
    assert(exists seq = seqs[0], seq.seq == "aToplevelFunction");
    assert(sequencedAnnotations(seqAnnotation, aToplevelGetterSetterDecl).size == 1);
    
    // TODO the annotations on the setter

}

void annotationTests() {
    checkToplevelValueAnnotations();
    checkToplevelGetterAnnotations();
}