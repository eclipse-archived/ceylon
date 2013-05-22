
import ceylon.language.metamodel{
    type, 
    annotations, optionalAnnotation, sequencedAnnotations,
    Annotation2 = Annotation, 
    ConstrainedAnnotation, OptionalAnnotation, SequencedAnnotation, 
    Annotated,
    ClassOrInterface, Class, Interface,
    Function, Value
}
import ceylon.language.metamodel.untyped {
    ValueDeclaration = Value,
    VariableDeclaration = Variable,
    UntypedDeclaration = Declaration,
    FunctionDeclaration = Function,
    ClassDeclaration = Class,
    ClassOrInterfaceDeclaration = ClassOrInterface,
    InterfaceDeclaration = Interface,
    Package
}

Package aPackage {
    value aClassType = type(AClass(""));
    value aClassDecl = aClassType.declaration;
    value pkg = aClassDecl.packageContainer;
    return pkg;
}
ValueDeclaration aToplevelValueDecl {
    assert(is ValueDeclaration result = aPackage.getAttribute("aToplevelValue"));
    return result;
}
VariableDeclaration aToplevelGetterSetterDecl {
    assert(is VariableDeclaration result = aPackage.getAttribute("aToplevelGetterSetter"));
    return result;
}
FunctionDeclaration aToplevelFunctionDecl {
    assert(is FunctionDeclaration result = aPackage.getFunction("aToplevelFunction"));
    return result;
}
ClassDeclaration aClassDecl {
    return type(AClass("")).declaration;
}
ClassDeclaration aAbstractClassDecl {
    assert(exists sup=aClassDecl.superclass);
    return sup.declaration;
}
InterfaceDeclaration aInterfaceDecl {
    assert(exists sup=aClassDecl.superclass);
    assert(exists iface0=sup.interfaces[0]);
    return iface0.declaration;
}

ClassOrInterface<T> annotationType<T>(T t) {
    return type(t);
}

ClassOrInterface<Shared> sharedAnnotation = annotationType(Shared());
ClassOrInterface<Abstract> abstractAnnotation = annotationType(Abstract());
ClassOrInterface<Formal> formalAnnotation = annotationType(Formal());
ClassOrInterface<Default> defaultAnnotation = annotationType(Default());
ClassOrInterface<Actual> actualAnnotation = annotationType(Actual());
ClassOrInterface<Variable> variableAnnotation = annotationType(Variable());
ClassOrInterface<Doc> docAnnotation = annotationType(Doc(""));
ClassOrInterface<Seq> seqAnnotation = annotationType(Seq(""));
ClassOrInterface<Deprecation> deprecatedAnnotation = annotationType(Deprecation(""));
ClassOrInterface<Optional> optAnnotation = annotationType(Optional());

void checkAToplevelValueAnnotations() {
    //shared
    assert(annotations(sharedAnnotation, aToplevelValueDecl) exists);
    assert(optionalAnnotation(sharedAnnotation, aToplevelValueDecl) exists);
    assert(aToplevelValueDecl.annotations<Shared>().size == 1);
    // doc
    assert(exists doc = annotations(docAnnotation, aToplevelValueDecl), 
        doc.description == "aToplevelValue");
    assert(exists doc2 = optionalAnnotation(docAnnotation, aToplevelValueDecl), 
        doc2.description == "aToplevelValue");
    assert(nonempty doc3 = aToplevelValueDecl.annotations<Doc>(),
        doc3.first.description == "aToplevelValue");
    // seq
    variable value seqs = annotations(seqAnnotation, aToplevelValueDecl);
    assert(seqs.size == 2);
    assert(exists seq = seqs[0], seq.seq == "aToplevelValue 1");
    assert(exists seq2 = seqs[1], seq2.seq == "aToplevelValue 2");
    assert(sequencedAnnotations(seqAnnotation, aToplevelValueDecl).size == 2);
    assert(aToplevelValueDecl.annotations<Seq>().size == 2);
    
    // Using funky type arguments to Declaration.annotations<>()
    assert(aToplevelValueDecl.annotations<Nothing>().empty);
    assert(aToplevelValueDecl.annotations<ConstrainedAnnotation<Nothing, Anything, ValueDeclaration>>() empty);
    assert(nonempty doc4 = aToplevelValueDecl.annotations<ConstrainedAnnotation<Doc, Anything, ValueDeclaration>>(),
        is Doc doc4_1 = doc4.first,
        doc4_1.description == "aToplevelValue");
    assert(nonempty doc5 = aToplevelValueDecl.annotations<OptionalAnnotation<Doc, ValueDeclaration>>(),
        is Doc doc5_1 = doc5.first,
        doc5_1.description == "aToplevelValue");
    // since Doc is not Sequenced, this returns empty:
    assert(aToplevelValueDecl.annotations<SequencedAnnotation<Doc, ValueDeclaration>>().empty);
    assert(nonempty shared6 = aToplevelValueDecl.annotations<OptionalAnnotation<Shared, ValueDeclaration>>(),
        is Shared shared6_1 = shared6.first);
    assert(nonempty seq7 = aToplevelValueDecl.annotations<SequencedAnnotation<Seq, ValueDeclaration>>(),
        is Seq seq7_1 = seq7.first,
        seq7_1.seq == "aToplevelValue 1");
}

void checkAToplevelGetterSetterAnnotations() {
    //shared
    assert(annotations(sharedAnnotation, aToplevelGetterSetterDecl) exists);
    assert(optionalAnnotation(sharedAnnotation, aToplevelGetterSetterDecl) exists);
    assert(aToplevelGetterSetterDecl.annotations<Shared>() nonempty);
    // doc
    assert(exists doc = annotations(docAnnotation, aToplevelGetterSetterDecl), 
        doc.description == "aToplevelGetter");
    assert(exists doc2 = optionalAnnotation(docAnnotation, aToplevelGetterSetterDecl), 
        doc2.description == "aToplevelGetter");
    assert(nonempty doc3 = aToplevelGetterSetterDecl.annotations<Doc>(),
        doc3.first.description == "aToplevelGetter");
    // seq
    variable value seqs = annotations(seqAnnotation, aToplevelGetterSetterDecl);
    assert(seqs.size == 1);
    assert(exists seq = seqs[0], seq.seq == "aToplevelGetter 1");
    assert(sequencedAnnotations(seqAnnotation, aToplevelGetterSetterDecl).size == 1);
    assert(nonempty seq2 = aToplevelGetterSetterDecl.annotations<Seq>(),
        seq2.first.seq == "aToplevelGetter 1");
    
    // setter
    assert(exists docsetter = optionalAnnotation(docAnnotation, aToplevelGetterSetterDecl.setter), 
        docsetter.description == "aToplevelSetter");
}

void checkAToplevelFunctionAnnotations() {
    //shared
    assert(annotations(sharedAnnotation, aToplevelFunctionDecl) exists);
    assert(optionalAnnotation(sharedAnnotation, aToplevelFunctionDecl) exists);
    assert(aToplevelFunctionDecl.annotations<Shared>() nonempty);
    // doc
    assert(exists doc = annotations(docAnnotation, aToplevelFunctionDecl), 
            doc.description == "aToplevelFunction");
    assert(exists doc2 = optionalAnnotation(docAnnotation, aToplevelFunctionDecl), 
            doc2.description == "aToplevelFunction");
    assert(nonempty doc3=aToplevelFunctionDecl.annotations<Doc>(),
            doc3.first.description == "aToplevelFunction");
    // seq
    variable value seqs = annotations(seqAnnotation, aToplevelFunctionDecl);
    assert(seqs.size == 1);
    assert(exists seq = seqs[0], 
            seq.seq == "aToplevelFunction 1");
    assert(sequencedAnnotations(seqAnnotation, aToplevelFunctionDecl).size == 1);
    assert(nonempty seq2=aToplevelFunctionDecl.annotations<Seq>(),
            seq2.first.seq == "aToplevelFunction 1");
    
    // parameter
    assert(exists parameter = aToplevelFunctionDecl.parameters[0]);
    // parameter doc
    assert(exists pdoc = annotations(docAnnotation, parameter),
            pdoc.description == "aToplevelFunction.parameter");
    // parameter seq
    value pseqs = annotations(seqAnnotation, parameter);
    assert(pseqs.size == 1);
    assert(exists pseq = pseqs[0],
            pseq.seq== "aToplevelFunction.parameter 1");
}

/*TODO test object declarations!
void checkAToplevelObjectAnnotations() {
    value aToplevelObjectDecl = type(aToplevelObject).declaration;
    //shared
    assert(annotations(sharedAnnotation, aToplevelObjectDecl) exists);
    assert(optionalAnnotation(sharedAnnotation, aToplevelObjectDecl) exists);
    assert(aToplevelObjectDecl.annotations<Shared>() nonempty);
    
    // doc
    assert(exists doc = annotations(docAnnotation, aToplevelObjectDecl), 
        doc.description == "aToplevelObject");
    assert(exists doc2 = optionalAnnotation(docAnnotation, aToplevelObjectDecl), 
        doc2.description == "aToplevelObject");
    assert(nonempty doc3 = aToplevelObjectDecl.annotations<Doc>(),
        doc3.first.description == "aToplevelObject");
    // seq
    variable value seqs = annotations(seqAnnotation, aToplevelObjectDecl);
    assert(seqs.size == 1);
    assert(exists seq = seqs[0], seq.seq == "aToplevelObject 1");
    assert(sequencedAnnotations(seqAnnotation, aToplevelObjectDecl).size == 1);
    assert(nonempty seq2 = aToplevelObjectDecl.annotations<Seq>(),
        seq2.first.seq == "aToplevelObject 1");
}*/

void checkAClass() {
    //shared
    assert(annotations(sharedAnnotation, aClassDecl) exists);
    assert(optionalAnnotation(sharedAnnotation, aClassDecl) exists);
    assert(aClassDecl.annotations<Shared>() nonempty);
    //abstract
    assert(! annotations(abstractAnnotation, aClassDecl) exists);
    assert(! optionalAnnotation(abstractAnnotation, aClassDecl) exists);
    assert(! aClassDecl.annotations<Abstract>() nonempty);
    // doc
    assert(exists doc = annotations(docAnnotation, aClassDecl), 
        doc.description == "AClass");
    assert(exists doc2 = optionalAnnotation(docAnnotation, aClassDecl), 
        doc2.description == "AClass");
    assert(nonempty doc3 = aClassDecl.annotations<Doc>(),
        doc3.first.description == "AClass");
    // seq
    variable value seqs = annotations(seqAnnotation, aClassDecl);
    assert(seqs.size == 2);
    assert(exists seq = seqs[0], seq.seq == "AClass 1");
    assert(exists seq2 = seqs[1], seq2.seq == "AClass 2");
    assert(sequencedAnnotations(seqAnnotation, aClassDecl).size == 2);
    assert(nonempty seq3 = aClassDecl.annotations<Seq>(),
        seq3.size == 2,
        seq3.first.seq == "AClass 1");
    
    // parameter
    assert(exists parameter = aClassDecl.parameters[0]);
    // parameter doc
    assert(exists pdoc = annotations(docAnnotation, parameter),
            pdoc.description == "AClass.parameter");
    // parameter seq
    value pseqs = annotations(seqAnnotation, parameter);
    assert(pseqs.size == 2);
    assert(exists pseq = pseqs[0],
            pseq.seq== "AClass.parameter 1");
    assert(exists pseq2 = pseqs[1],
            pseq2.seq== "AClass.parameter 2");
}

void checkAAbstractClass() {
    //shared
    assert(annotations(sharedAnnotation, aAbstractClassDecl) exists);
    assert(optionalAnnotation(sharedAnnotation, aAbstractClassDecl) exists);
    //abstract
    assert(annotations(abstractAnnotation, aAbstractClassDecl) exists);
    assert(optionalAnnotation(abstractAnnotation, aAbstractClassDecl) exists);
    // doc
    assert(exists doc = annotations(docAnnotation, aAbstractClassDecl), 
        doc.description == "AAbstractClass");
    assert(exists doc2 = optionalAnnotation(docAnnotation, aAbstractClassDecl), 
        doc2.description == "AAbstractClass");
    // seq
    variable value seqs = annotations(seqAnnotation, aAbstractClassDecl);
    assert(seqs.size == 2);
    assert(exists seq = seqs[0], seq.seq == "AAbstractClass 1");
    assert(exists seq2 = seqs[1], seq2.seq == "AAbstractClass 2");
    assert(sequencedAnnotations(seqAnnotation, aAbstractClassDecl).size == 2);
    
    // parameter
    assert(exists parameter = aAbstractClassDecl.parameters[0]);
    // parameter doc
    assert(exists pdoc = annotations(docAnnotation, parameter),
            pdoc.description == "AAbstractClass.parameter");
    // parameter seq
    value pseqs = annotations(seqAnnotation, parameter);
    assert(pseqs.size == 0);
    
    // Members of abstract class
    // formalAttribute
    assert(exists fam=aAbstractClassDecl.apply().getAttribute<AAbstractClass, Value<String>>("formalAttribute"));
    assert(is VariableDeclaration fa=fam(AClass("")).declaration);
    assert(annotations(sharedAnnotation, fa) exists);
    assert(annotations(actualAnnotation, fa) exists);
    assert(exists fadoc = annotations(docAnnotation, fa),
            fadoc.description == "AAbstractClass.formalAttributeGetter");
    assert(exists fasdoc = annotations(docAnnotation, fa.setter),
            fasdoc.description == "AAbstractClass.formalAttributeSetter");
    
    // formalMethod
    assert(exists fmm=aAbstractClassDecl.apply().getFunction<AAbstractClass, Function<Anything, [String]>>("formalMethod"));
    value fm=fmm(AClass("")).declaration;
    // shared
    assert(annotations(sharedAnnotation, fm) exists);
    // actual
    assert(annotations(actualAnnotation, fm) exists);
    // default
    assert(annotations(defaultAnnotation, fm) exists);
    // doc
    assert(exists fmdoc = annotations(docAnnotation, fm),
            fmdoc.description == "AAbstractClass.formalMethod");
    // parameter
    assert(exists fmp = fm.parameters[0]);
    // parameter doc
    assert(exists fmpdoc = annotations(docAnnotation, fmp),
            fmpdoc.description == "AAbstractClass.formalMethod.parameter");
    
    // InnerClass
    assert(exists icm=aAbstractClassDecl.apply().getClassOrInterface<AAbstractClass, Class<AAbstractClass.InnerClass, [String]>>("InnerClass"));
    value ic=icm(AClass("")).declaration;
    // shared
    assert(annotations(sharedAnnotation, ic) exists);
    // shared
    assert(exists icdoc = annotations(docAnnotation, ic));
    assert(icdoc.description == "AAbstractClass.InnerClass");
    // InnerClass parameter
    assert(exists icparameter = ic.parameters[0]);
    // InnerClass parameter doc
    assert(exists icpdoc = annotations(docAnnotation, icparameter),
            icpdoc.description == "AAbstractClass.InnerClass.parameter");
    // TODO inner classes methods
    
    // InnerInterface
    assert(exists iim=aAbstractClassDecl.apply().getClassOrInterface<AAbstractClass, Interface<AAbstractClass.InnerInterface>>("InnerInterface"));
    value ii=iim(AClass("")).declaration;
    // shared
    assert(annotations(sharedAnnotation, ii) exists);
    // shared
    assert(exists iidoc = annotations(docAnnotation, ii));
    assert(iidoc.description == "AAbstractClass.InnerInterface");
    // TODO inner interfaces methods
    
    
    // TODO Object members
}

void checkAInterface() {
    assert(is Interface<AInterface> iface = aInterfaceDecl.apply());
    //shared
    assert(annotations(sharedAnnotation, aInterfaceDecl) exists);
    assert(optionalAnnotation(sharedAnnotation, aInterfaceDecl) exists);
    //abstract
    //assert(! annotations(abstractAnnotation, aInterfaceDecl.declaration) exists);
    //assert(! optionalAnnotation(abstractAnnotation, aInterfaceDecl.declaration) exists);
    // doc
    assert(exists doc = annotations(docAnnotation, aInterfaceDecl), 
            doc.description == "AInterface");
    assert(exists doc2 = optionalAnnotation(docAnnotation, aInterfaceDecl), 
            doc2.description == "AInterface");
    // seq
    variable value seqs = annotations(seqAnnotation, aInterfaceDecl);
    assert(seqs.size == 2);
    assert(exists seq = seqs[0], 
            seq.seq == "AInterface 1");
    assert(exists seq2 = seqs[1], 
            seq2.seq == "AInterface 2");
    assert(sequencedAnnotations(seqAnnotation, aInterfaceDecl).size == 2);
    
    // Members of interface
    // formalAttribute
    assert(exists fam=iface.getAttribute<AInterface, Value<String>>("formalAttribute"));
    value fa = fam(AClass("")).declaration;
    assert(annotations(sharedAnnotation, fa) exists);
    assert(exists fadoc = annotations(docAnnotation, fa),
            fadoc.description == "AInterface.formalAttribute");
    
    // defaultGetterSetter
    assert(exists dgsm=iface.getAttribute<AInterface, Value<String>>("defaultGetterSetter"));
    assert(is VariableDeclaration dgs = dgsm(AClass("")).declaration);
    assert(annotations(sharedAnnotation, dgs) exists);
    assert(annotations(defaultAnnotation, dgs) exists);
    assert(exists dgdoc = annotations(docAnnotation, dgs),
            dgdoc.description == "AInterface.defaultGetter");
    assert(exists dsdoc = annotations(docAnnotation, dgs.setter),
            dsdoc.description == "AInterface.defaultSetter");
    
    // getterSetter
    assert(exists gsm=iface.getAttribute<AInterface, Value<String>>("getterSetter"));
    assert(is VariableDeclaration gs = gsm(AClass("")).declaration);
    assert(annotations(sharedAnnotation, gs) exists);
    assert(exists gsdoc = annotations(docAnnotation, gs),
            gsdoc.description == "AInterface.getter");
    // setter annotations
    assert(exists gssdoc = annotations(docAnnotation, gs.setter),
            gssdoc.description == "AInterface.setter");
    
    // TODO nonsharedGetterSetter
    //assert(exists ngsm=iface.getAttribute<AInterface, Value<String>>("nonsharedGetterSetter"));
    //value ngs=ngsm(AClass(""));
    //assert(annotations(sharedAnnotation, ngs) exists);
    //assert(exists ngsdoc = annotations(docAnnotation, ngs),
    //        ngsdoc.description == "AInterface.nonsharedGetterSetter");
    // TODO Test the setter annotations
    
    // TODO Class & interface members
    // TODO Object members
}

void checkModuleAndImports() {
    value m = aPackage.container;
    assert(exists moddoc = annotations(docAnnotation, m));
    assert(moddoc.description == "Some module doc");
    
    // module imports
    value deps = m.dependencies;
    assert(1 == deps.size);
    assert(exists dep = deps[0]);
    assert("java.base" == dep.name);
    assert("7" == dep.version);
    assert(exists depdoc = annotations(docAnnotation, dep));
    assert(depdoc.description == "Not actually needed, but we want to test ModuleImports");
    assert(annotations(optAnnotation, dep) exists);
    assert(annotations(deprecatedAnnotation, dep) exists);
    
}

void checkPackage() {
    value p = aPackage;
    assert(! annotations(sharedAnnotation, p) exists);
    assert(! annotations(docAnnotation, p) exists);
}

void annotationTests() {
    checkAToplevelValueAnnotations();
    checkAToplevelGetterSetterAnnotations();
    checkAToplevelFunctionAnnotations();
    // TODO once we support objects
    // checkAToplevelObjectAnnotations();
    checkAInterface();
    checkAAbstractClass();
    checkAClass();
    // TODO Local declarations
    checkModuleAndImports();
    checkPackage();
    
}