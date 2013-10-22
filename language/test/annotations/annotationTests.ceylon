import check { check,results,fail }
import ceylon.language.meta{
    type, 
    annotations, optionalAnnotation, sequencedAnnotations
}
import ceylon.language.meta.model{
    ClassOrInterface, Class, Interface,
    Function, Attribute
}
import ceylon.language.meta.declaration {
    ValueDeclaration,
    VariableDeclaration,
    NestableDeclaration,
    FunctionDeclaration,
    ClassDeclaration,
    ClassOrInterfaceDeclaration,
    InterfaceDeclaration,
    Package
}

Package aPackage {
    value aClassType = type(AClass(""));
    value aClassDecl = aClassType.declaration;
    value pkg = aClassDecl.containingPackage;
    return pkg;
}
ValueDeclaration aToplevelAttributeDecl {
    assert(is ValueDeclaration result = aPackage.getValue("aToplevelAttribute"));
    return result;
}
VariableDeclaration aToplevelGetterSetterDecl {
    assert(is VariableDeclaration result = aPackage.getValue("aToplevelGetterSetter"));
    return result;
}
ValueDeclaration aToplevelObjectDecl {
    assert(is ValueDeclaration result = aPackage.getValue("aToplevelObject"));
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
    assert(exists sup=aClassDecl.extendedType);
    return sup.declaration;
}
InterfaceDeclaration aInterfaceDecl {
    assert(exists sup=aClassDecl.extendedType);
    assert(exists iface0=sup.satisfiedTypes[0]);
    return iface0.declaration;
}
ClassDeclaration memberClassDecl(ClassOrInterfaceDeclaration outerClass, String memberName) {
    for (ClassDeclaration cDecl in outerClass.memberDeclarations<ClassDeclaration>()) {
        if (cDecl.name == memberName) {
            return cDecl;
        }
    } 
    throw;
}
InterfaceDeclaration memberInterfaceDecl(ClassOrInterfaceDeclaration outerClass, String memberName) {
    for (InterfaceDeclaration iDecl in outerClass.memberDeclarations<InterfaceDeclaration>()) {
        if (iDecl.name == memberName) {
            return iDecl;
        }
    } 
    throw;
}

ClassOrInterface<T> annotationType<T>(T t) {
    return type(t);
}

Class<SharedAnnotation,[]> sharedAnnotation = `SharedAnnotation`;
Class<AbstractAnnotation,[]> abstractAnnotation = `AbstractAnnotation`;
Class<FormalAnnotation,[]> formalAnnotation = `FormalAnnotation`;
Class<DefaultAnnotation,[]> defaultAnnotation = `DefaultAnnotation`;
Class<ActualAnnotation,[]> actualAnnotation = `ActualAnnotation`;
Class<VariableAnnotation,[]> variableAnnotation = `VariableAnnotation`;
Class<DocAnnotation,[String]> docAnnotation = `DocAnnotation`;
Class<SeeAnnotation,[]> seeAnnotation = `SeeAnnotation`;
Class<Seq,[String]> seqAnnotation = `Seq`;
Class<Enumerated,[Comparison]> enumeratedAnnotation = `Enumerated`;
Class<EnumeratedVariadic,[]> enumeratedVariadicAnnotation = `EnumeratedVariadic`;
Class<DeprecationAnnotation,[String]> deprecatedAnnotation = `DeprecationAnnotation`;
Class<OptionalImportAnnotation,[]> optAnnotation = `OptionalImportAnnotation`;

@test
shared void checkAToplevelAttributeAnnotations() {
    //shared
    check(annotations(sharedAnnotation, aToplevelAttributeDecl) exists);
    check(optionalAnnotation(sharedAnnotation, aToplevelAttributeDecl) exists);
    check(aToplevelAttributeDecl.annotations<SharedAnnotation>().size == 1);
    // doc
    if (exists doc = annotations(docAnnotation, aToplevelAttributeDecl), 
        doc.description == "aToplevelAttribute"){}else{fail("aToplevelAttributeDecl doc == 'aToplevelAttribute' 1");}
    if (exists doc2 = optionalAnnotation(docAnnotation, aToplevelAttributeDecl), 
        doc2.description == "aToplevelAttribute"){}else{fail("aToplevelAttributeDecl doc=='aToplevelAttribute' 2");}
    if (nonempty doc3 = aToplevelAttributeDecl.annotations<DocAnnotation>(),
        doc3.first.description == "aToplevelAttribute"){}else{fail("aToplevelAttributeDecl doc='aToplevelAttribute' 3");}
    // seq
    variable value seqs = annotations(seqAnnotation, aToplevelAttributeDecl);
    check(seqs.size == 2);
    assert(exists seq = seqs[0], seq.seq == "aToplevelAttribute 1");
    assert(exists seq2 = seqs[1], seq2.seq == "aToplevelAttribute 2");
    check(sequencedAnnotations(seqAnnotation, aToplevelAttributeDecl).size == 2);
    check(aToplevelAttributeDecl.annotations<Seq>().size == 2);
    
    // Using funky type arguments to Declaration.annotations<>()
    check(aToplevelAttributeDecl.annotations<Nothing>().empty);
    // TODO Depends on fix for #1157 assert(aToplevelAttributeDecl.annotations<ConstrainedAnnotation<Nothing, Anything, ValueDeclaration>>() empty);
    assert(nonempty doc4 = aToplevelAttributeDecl.annotations<ConstrainedAnnotation<DocAnnotation, Anything, ValueDeclaration>>(),
        is DocAnnotation doc4_1 = doc4.first,
        doc4_1.description == "aToplevelAttribute");
    assert(nonempty doc5 = aToplevelAttributeDecl.annotations<OptionalAnnotation<DocAnnotation, ValueDeclaration>>(),
        is DocAnnotation doc5_1 = doc5.first,
        doc5_1.description == "aToplevelAttribute");
    check(aToplevelAttributeDecl.annotations<SharedAnnotation|DocAnnotation|Seq>().size == 4);
    check(aToplevelAttributeDecl.annotations<SharedAnnotation|DocAnnotation>().size == 2);
    check(aToplevelAttributeDecl.annotations<DocAnnotation|Seq>().size == 3);
    
    // since DocAnnotation is not Sequenced, this returns empty:
    assert(nonempty shared6 = aToplevelAttributeDecl.annotations<OptionalAnnotation<SharedAnnotation, ValueDeclaration>>(),
        is SharedAnnotation shared6_1 = shared6.first);
    assert(nonempty seq7 = aToplevelAttributeDecl.annotations<SequencedAnnotation<Seq, ValueDeclaration>>(),
        is Seq seq7_1 = seq7.first,
        seq7_1.seq == "aToplevelAttribute 1");
    
    assert(nonempty see = annotations(seeAnnotation, aToplevelAttributeDecl));
    check(see.size == 1);
    check(see.first.programElements.size == 3);
    check(see.first.programElements.contains(`value aToplevelGetterSetter`));
    check(see.first.programElements.contains(`module ceylon.language`));
    check(see.first.programElements.contains(`package ceylon.language.meta.declaration`));
    
    
    assert(exists enumed = annotations(enumeratedAnnotation, aToplevelAttributeDecl));
    check(enumed.c == larger);
    
    assert(exists enumedVariadic = annotations(enumeratedVariadicAnnotation, aToplevelAttributeDecl));
    check(enumedVariadic.c.size == 3);
    assert(exists ev0 = enumedVariadic.c[0],
        ev0 == larger);
    assert(exists ev1 = enumedVariadic.c[1],
        ev1 == equal);
    assert(exists ev2 = enumedVariadic.c[2],
        ev2 == smaller);
}

@test
shared void checkAToplevelGetterSetterAnnotations() {
    //shared
    check(annotations(sharedAnnotation, aToplevelGetterSetterDecl) exists);
    check(optionalAnnotation(sharedAnnotation, aToplevelGetterSetterDecl) exists);
    check(aToplevelGetterSetterDecl.annotations<SharedAnnotation>() nonempty);
    // doc
    assert(exists doc = annotations(docAnnotation, aToplevelGetterSetterDecl), 
        doc.description == "aToplevelGetter");
    assert(exists doc2 = optionalAnnotation(docAnnotation, aToplevelGetterSetterDecl), 
        doc2.description == "aToplevelGetter");
    assert(nonempty doc3 = aToplevelGetterSetterDecl.annotations<DocAnnotation>(),
        doc3.first.description == "aToplevelGetter");
    // seq
    variable value seqs = annotations(seqAnnotation, aToplevelGetterSetterDecl);
    check(seqs.size == 1);
    assert(exists seq = seqs[0], seq.seq == "aToplevelGetter 1");
    check(sequencedAnnotations(seqAnnotation, aToplevelGetterSetterDecl).size == 1);
    assert(nonempty seq2 = aToplevelGetterSetterDecl.annotations<Seq>(),
        seq2.first.seq == "aToplevelGetter 1");
    
    // setter
    assert(exists docsetter = optionalAnnotation(docAnnotation, aToplevelGetterSetterDecl.setter), 
        docsetter.description == "aToplevelSetter");
    
    assert(nonempty see = annotations(seeAnnotation, aToplevelGetterSetterDecl));
    check(see.size == 1);
    check(see.first.programElements.contains(`value aToplevelAttribute`));
}

@test
shared void checkAToplevelFunctionAnnotations() {
    //shared
    check(annotations(sharedAnnotation, aToplevelFunctionDecl) exists);
    check(optionalAnnotation(sharedAnnotation, aToplevelFunctionDecl) exists);
    check(aToplevelFunctionDecl.annotations<SharedAnnotation>() nonempty);
    // doc
    assert(exists doc = annotations(docAnnotation, aToplevelFunctionDecl), 
            doc.description == "aToplevelFunction");
    assert(exists doc2 = optionalAnnotation(docAnnotation, aToplevelFunctionDecl), 
            doc2.description == "aToplevelFunction");
    assert(nonempty doc3=aToplevelFunctionDecl.annotations<DocAnnotation>(),
            doc3.first.description == "aToplevelFunction");
    // seq
    variable value seqs = annotations(seqAnnotation, aToplevelFunctionDecl);
    check(seqs.size == 1);
    assert(exists seq = seqs[0], 
            seq.seq == "aToplevelFunction 1");
    assert(sequencedAnnotations(seqAnnotation, aToplevelFunctionDecl).size == 1);
    assert(nonempty seq2=aToplevelFunctionDecl.annotations<Seq>(),
            seq2.first.seq == "aToplevelFunction 1");
    
    // parameter
    assert(exists parameter = aToplevelFunctionDecl.parameterDeclarations[0]);
    // parameter doc
    assert(exists pdoc = annotations(docAnnotation, parameter),
            pdoc.description == "aToplevelFunction.parameter");
    // parameter seq
    value pseqs = annotations(seqAnnotation, parameter);
    check(pseqs.size == 1);
    assert(exists pseq = pseqs[0],
            pseq.seq== "aToplevelFunction.parameter 1");
}

@test
shared void checkAToplevelObjectAnnotations() {
    
    //shared
    check(annotations(sharedAnnotation, aToplevelObjectDecl) exists);
    check(optionalAnnotation(sharedAnnotation, aToplevelObjectDecl) exists);
    check(aToplevelObjectDecl.annotations<SharedAnnotation>() nonempty);
    
    // doc
    assert(exists doc = annotations(docAnnotation, aToplevelObjectDecl), 
        doc.description == "aToplevelObject");
    assert(exists doc2 = optionalAnnotation(docAnnotation, aToplevelObjectDecl), 
        doc2.description == "aToplevelObject");
    assert(nonempty doc3 = aToplevelObjectDecl.annotations<DocAnnotation>(),
        doc3.first.description == "aToplevelObject");
    // seq
    variable value seqs = annotations(seqAnnotation, aToplevelObjectDecl);
    check(seqs.size == 1);
    assert(exists seq = seqs[0], seq.seq == "aToplevelObject 1");
    check(sequencedAnnotations(seqAnnotation, aToplevelObjectDecl).size == 1);
    assert(nonempty seq2 = aToplevelObjectDecl.annotations<Seq>(),
        seq2.first.seq == "aToplevelObject 1");
}

@test
shared void checkAClass() {
    //shared
    check(annotations(sharedAnnotation, aClassDecl) exists);
    check(optionalAnnotation(sharedAnnotation, aClassDecl) exists);
    check(aClassDecl.annotations<SharedAnnotation>() nonempty);
    //abstract
    check(! annotations(abstractAnnotation, aClassDecl) exists);
    check(! optionalAnnotation(abstractAnnotation, aClassDecl) exists);
    check(! aClassDecl.annotations<AbstractAnnotation>() nonempty);
    // doc
    assert(exists doc = annotations(docAnnotation, aClassDecl), 
        doc.description == "AClass");
    assert(exists doc2 = optionalAnnotation(docAnnotation, aClassDecl), 
        doc2.description == "AClass");
    assert(nonempty doc3 = aClassDecl.annotations<DocAnnotation>(),
        doc3.first.description == "AClass");
    // seq
    variable value seqs = annotations(seqAnnotation, aClassDecl);
    check(seqs.size == 2);
    assert(exists seq = seqs[0], seq.seq == "AClass 1");
    assert(exists seq2 = seqs[1], seq2.seq == "AClass 2");
    check(sequencedAnnotations(seqAnnotation, aClassDecl).size == 2);
    assert(nonempty seq3 = aClassDecl.annotations<Seq>(),
        seq3.size == 2,
        seq3.first.seq == "AClass 1");
    
    // parameter
    assert(exists parameter = aClassDecl.parameterDeclarations[0]);
    // parameter doc
    assert(exists pdoc = annotations(docAnnotation, parameter),
            pdoc.description == "AClass.parameter");
    // parameter seq
    value pseqs = annotations(seqAnnotation, parameter);
    check(pseqs.size == 2);
    assert(exists pseq = pseqs[0],
            pseq.seq== "AClass.parameter 1");
    assert(exists pseq2 = pseqs[1],
            pseq2.seq== "AClass.parameter 2");
    
    
    assert(exists aca1doc = annotations(docAnnotation, `class AClass.DefaultInnerClassAlias1`),
        aca1doc.description == "AClass.DefaultInnerClassAlias1");
    
    assert(
        exists aca1pDecl = `class AClass.DefaultInnerClassAlias1`.parameterDeclarations[0],
        exists aca1pdoc = annotations(docAnnotation, aca1pDecl),
        aca1pdoc.description == "AClass.DefaultInnerClassAlias1.parameter");
    
    assert(exists aca2doc = annotations(docAnnotation, `class AClass.DefaultInnerClassAlias2`),
        aca2doc.description == "AClass.DefaultInnerClassAlias2");
    
    assert(
        exists aca2pDecl = `class AClass.DefaultInnerClassAlias2`.parameterDeclarations[0],
        exists aca2pdoc = annotations(docAnnotation, aca2pDecl),
        aca2pdoc.description == "AClass.DefaultInnerClassAlias2.parameter");
    
}

@test
shared void checkAAbstractClass() {
    //shared
    check(annotations(sharedAnnotation, aAbstractClassDecl) exists);
    check(optionalAnnotation(sharedAnnotation, aAbstractClassDecl) exists);
    //abstract
    check(annotations(abstractAnnotation, aAbstractClassDecl) exists);
    check(optionalAnnotation(abstractAnnotation, aAbstractClassDecl) exists);
    // doc
    assert(exists doc = annotations(docAnnotation, aAbstractClassDecl), 
        doc.description == "AAbstractClass");
    assert(exists doc2 = optionalAnnotation(docAnnotation, aAbstractClassDecl), 
        doc2.description == "AAbstractClass");
    // seq
    variable value seqs = annotations(seqAnnotation, aAbstractClassDecl);
    check(seqs.size == 2);
    assert(exists seq = seqs[0], seq.seq == "AAbstractClass 1");
    assert(exists seq2 = seqs[1], seq2.seq == "AAbstractClass 2");
    assert(sequencedAnnotations(seqAnnotation, aAbstractClassDecl).size == 2);
    
    // parameter
    assert(exists parameter = aAbstractClassDecl.parameterDeclarations[0]);
    // parameter doc
    assert(exists pdoc = annotations(docAnnotation, parameter),
            pdoc.description == "AAbstractClass.parameter");
    // parameter seq
    value pseqs = annotations(seqAnnotation, parameter);
    check(pseqs.size == 0);
    
    // Members of abstract class
    // formalAttribute
    assert(exists fam=aAbstractClassDecl.apply<AAbstractClass>().getAttribute<AAbstractClass, String>("formalAttribute"));
    assert(is VariableDeclaration fa=fam(AClass("")).declaration);
    check(annotations(sharedAnnotation, fa) exists);
    check(annotations(actualAnnotation, fa) exists);
    assert(exists fadoc = annotations(docAnnotation, fa),
            fadoc.description == "AAbstractClass.formalAttributeGetter");
    assert(exists fasdoc = annotations(docAnnotation, fa.setter),
            fasdoc.description == "AAbstractClass.formalAttributeSetter");
    
    // formalMethod
    assert(exists fmm=aAbstractClassDecl.apply<AAbstractClass>().getMethod<AAbstractClass, Anything, [String]>("formalMethod"));
    value fm=fmm(AClass("")).declaration;
    // shared
    check(annotations(sharedAnnotation, fm) exists);
    // actual
    check(annotations(actualAnnotation, fm) exists);
    // default
    check(annotations(defaultAnnotation, fm) exists);
    // doc
    assert(exists fmdoc = annotations(docAnnotation, fm),
            fmdoc.description == "AAbstractClass.formalMethod");
    // parameter
    assert(exists fmp = fm.parameterDeclarations[0]);
    // parameter doc
    assert(exists fmpdoc = annotations(docAnnotation, fmp),
            fmpdoc.description == "AAbstractClass.formalMethod.parameter");
    
    // InnerClass
    assert(exists icm=aAbstractClassDecl.apply<AAbstractClass>().getClassOrInterface<AAbstractClass, Class<AAbstractClass.InnerClass, [String]>>("InnerClass"));
    value ic=icm(AClass("")).declaration;
    // shared
    check(annotations(sharedAnnotation, ic) exists);
    // shared
    assert(exists icdoc = annotations(docAnnotation, ic));
    check(icdoc.description == "AAbstractClass.InnerClass");
    // InnerClass parameter
    assert(exists icparameter = ic.parameterDeclarations[0]);
    // InnerClass parameter doc
    assert(exists icpdoc = annotations(docAnnotation, icparameter),
            icpdoc.description == "AAbstractClass.InnerClass.parameter");
    // InnerClass method doc
    assert(exists icmdoc = annotations(docAnnotation, `function AAbstractClass.InnerClass.method`),
            icmdoc.description == "AAbstractClass.InnerClass.method");
    // InnerClass method param doc
    assert(
            exists icmpDecl = `function AAbstractClass.InnerClass.method`.parameterDeclarations[0],
            exists icmpdoc = annotations(docAnnotation, icmpDecl),
            icmpdoc.description == "AAbstractClass.InnerClass.method.parameter");
    
    // InnerInterface
    assert(exists iim=aAbstractClassDecl.apply<AAbstractClass>().getClassOrInterface<AAbstractClass, Interface<AAbstractClass.InnerInterface>>("InnerInterface"));
    value ii=iim(AClass("")).declaration;
    // shared
    check(annotations(sharedAnnotation, ii) exists);
    // shared
    assert(exists iidoc = annotations(docAnnotation, ii));
    check(iidoc.description == "AAbstractClass.InnerInterface");
    // InnerInterface method doc
    assert(exists iimdoc = annotations(docAnnotation, `function AAbstractClass.InnerInterface.method`),
    iimdoc.description == "AAbstractClass.InnerInterface.method");
    // InnerInterface method parameter doc
    assert(exists iimpDecl = `function AAbstractClass.InnerInterface.method`.parameterDeclarations[0],
    exists iimpdoc = annotations(docAnnotation, iimpDecl),
            iimpdoc.description == "AAbstractClass.InnerInterface.method.parameter");
    
    // objectMember
    assert(exists omdoc = annotations(docAnnotation, `value AAbstractClass.objectMember`),
    omdoc.description == "AAbstractClass.objectMember");
}

@test
shared void checkAInterface() {
    assert(is Interface<AInterface> iface = aInterfaceDecl.apply<AInterface>());
    //shared
    check(annotations(sharedAnnotation, aInterfaceDecl) exists);
    check(optionalAnnotation(sharedAnnotation, aInterfaceDecl) exists);
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
    check(seqs.size == 2);
    assert(exists seq = seqs[0], 
            seq.seq == "AInterface 1");
    assert(exists seq2 = seqs[1], 
            seq2.seq == "AInterface 2");
    check(sequencedAnnotations(seqAnnotation, aInterfaceDecl).size == 2);
    
    // Members of interface
    // formalAttribute
    assert(exists fam=iface.getAttribute<AInterface, String>("formalAttribute"));
    value fa = fam(AClass("")).declaration;
    check(annotations(sharedAnnotation, fa) exists);
    assert(exists fadoc = annotations(docAnnotation, fa),
            fadoc.description == "AInterface.formalAttribute");
    
    // defaultGetterSetter
    assert(exists dgsm=iface.getAttribute<AInterface, String>("defaultGetterSetter"));
    assert(is VariableDeclaration dgs = dgsm(AClass("")).declaration);
    check(annotations(sharedAnnotation, dgs) exists);
    check(annotations(defaultAnnotation, dgs) exists);
    assert(exists dgdoc = annotations(docAnnotation, dgs),
            dgdoc.description == "AInterface.defaultGetter");
    assert(exists dsdoc = annotations(docAnnotation, dgs.setter),
            dsdoc.description == "AInterface.defaultSetter");
    
    // getterSetter
    assert(exists gsm=iface.getAttribute<AInterface, String>("getterSetter"));
    assert(is VariableDeclaration gs = gsm(AClass("")).declaration);
    check(annotations(sharedAnnotation, gs) exists);
    assert(exists gsdoc = annotations(docAnnotation, gs),
            gsdoc.description == "AInterface.getter");
    // setter annotations
    assert(exists gssdoc = annotations(docAnnotation, gs.setter),
            gssdoc.description == "AInterface.setter");
    
    // TODO nonsharedGetterSetter
    //assert(exists ngsm=iface.getAttribute<AInterface, Attribute<String>>("nonsharedGetterSetter"));
    //value ngs=ngsm(AClass(""));
    //assert(annotations(sharedAnnotation, ngs) exists);
    //assert(exists ngsdoc = annotations(docAnnotation, ngs),
    //        ngsdoc.description == "AInterface.nonsharedGetterSetter");
    // TODO Test the setter annotations
    
    // formalMethod
    assert(exists fm=iface.getMethod<AInterface, Anything,[String]>("formalMethod"));
    value fmd = fm(AClass("")).declaration;
    assert(exists idfmdoc = annotations(docAnnotation, `function AInterface.formalMethod`),
            idfmdoc.description == "AInterface.formalMethod");
    // defaultMethod
    assert(exists dm=iface.getMethod<AInterface, Anything,[String]>("defaultMethod"));
    value dmd = dm(AClass("")).declaration;
    assert(exists iddmdoc = annotations(docAnnotation, `function AInterface.defaultMethod`),
            iddmdoc.description == "AInterface.defaultMethod");
    // method
    assert(exists m=iface.getMethod<AInterface, Anything,[String]>("method"));
    value md = m(AClass("")).declaration;
    assert(exists idmdoc = annotations(docAnnotation, `function AInterface.method`),
            idmdoc.description == "AInterface.method");
    // TODO nonsharedMethod
    //assert(exists nsm=iface.getFunction<AInterface, Anything,[String]>("nonsharedMethod"));
    //value nsmd = nsm(AClass("")).declaration;
    //assert(exists insmdoc = annotations(docAnnotation, `function AInterface.nonsharedMethod`),
    //        insmdoc.description == "AInterface.method");
    
    // Class & interface members
    variable ClassDeclaration ficd = memberClassDecl(aInterfaceDecl, "FormalInnerClass");
    assert(exists ificdoc = annotations(docAnnotation, `class AInterface.FormalInnerClass`),
            ificdoc.description == "AInterface.FormalInnerClass");
    variable ClassDeclaration dicd = memberClassDecl(aInterfaceDecl, "DefaultInnerClass");
    assert(exists idicdoc = annotations(docAnnotation, `class AInterface.DefaultInnerClass`),
            idicdoc.description == "AInterface.DefaultInnerClass");
    variable InterfaceDeclaration iid = memberInterfaceDecl(aInterfaceDecl, "SharedInnerInterface");
    assert(exists iiidoc = annotations(docAnnotation, `interface AInterface.SharedInnerInterface`),
            iiidoc.description == "AInterface.SharedInnerInterface");
    
    // Tests for annotatedMembers()
    value sharedClasses = aInterfaceDecl.annotatedMemberDeclarations<ClassDeclaration, SharedAnnotation>();
    check(ficd in sharedClasses);
    check(dicd in sharedClasses);
    check(! iid in sharedClasses);
    check(! fa in sharedClasses);
    check(! gs in sharedClasses);
    // TODO assert(! ngs in sharedClasses);
    check(! fmd in sharedClasses);
    check(! dmd in sharedClasses);
    check(! fmd in sharedClasses);
    check(! md in sharedClasses);
    // TODO assert(! nsmd in sharedClasses);
    // TODO test with an object declaration
    
    value sharedInterfaces = aInterfaceDecl.annotatedMemberDeclarations<InterfaceDeclaration, SharedAnnotation>();
    check(!ficd in sharedInterfaces);
    check(!dicd in sharedInterfaces);
    check(iid in sharedInterfaces);
    check(! fa in sharedInterfaces);
    check(! gs in sharedInterfaces);
    // TODO assert(! ngs in sharedInterfaces);
    check(! fmd in sharedInterfaces);
    check(! dmd in sharedInterfaces);
    check(! fmd in sharedInterfaces);
    check(! md in sharedInterfaces);
    // TODO assert(! nsmd in sharedInterfaces);
    // TODO test with an object declaration
    
    value sharedClassesAndInterfaces = aInterfaceDecl.annotatedMemberDeclarations<ClassOrInterfaceDeclaration, SharedAnnotation>();
    check(ficd in sharedClassesAndInterfaces);
    check(dicd in sharedClassesAndInterfaces);
    check(iid in sharedClassesAndInterfaces);
    check(! fa in sharedClassesAndInterfaces);
    check(! gs in sharedClassesAndInterfaces);
    // TODO assert(! ngs in sharedClassesAndInterfaces);
    check(! fmd in sharedClassesAndInterfaces);
    check(! dmd in sharedClassesAndInterfaces);
    check(! fmd in sharedClassesAndInterfaces);
    check(! md in sharedClassesAndInterfaces);
    // TODO assert(! nsmd in sharedInterfaces);
    // TODO test with an object declaration
    
    value sharedAttributes = aInterfaceDecl.annotatedMemberDeclarations<ValueDeclaration, SharedAnnotation>();
    check(! ficd in sharedAttributes);
    check(! dicd in sharedAttributes);
    check(! iid in sharedAttributes);
    check(fa in sharedAttributes);
    check(gs in sharedAttributes);
    // TODO assert(ngs in sharedAttributes);
    check(! fmd in sharedAttributes);
    check(! dmd in sharedAttributes);
    check(! fmd in sharedAttributes);
    check(! md in sharedAttributes);
    // TODO assert(! nsmd in sharedAttribute);
    // TODO test with an object declaration
    
    value sharedVariables = aInterfaceDecl.annotatedMemberDeclarations<VariableDeclaration, SharedAnnotation>();
    check(! ficd in sharedVariables);
    check(! dicd in sharedVariables);
    check(! iid in sharedVariables);
    check(! fa in sharedVariables);
    check(gs in sharedVariables);
    // TODO assert(ngs in sharedVariables);
    check(! fmd in sharedVariables);
    check(! dmd in sharedVariables);
    check(! fmd in sharedVariables);
    check(! md in sharedVariables);
    // TODO assert(! nsmd in sharedVariables);
    // TODO test with an object declaration
    
    value sharedMethods = aInterfaceDecl.annotatedMemberDeclarations<FunctionDeclaration, SharedAnnotation>();
    check(! ficd in sharedMethods);
    check(! dicd in sharedMethods);
    check(! iid in sharedMethods);
    check(! fa in sharedMethods);
    check(! gs in sharedMethods);
    // TODO assert(! ngs in sharedMethods);
    check(fmd in sharedMethods);
    check(dmd in sharedMethods);
    check(fmd in sharedMethods);
    check(md in sharedMethods);
    // TODO assert(nsmd in sharedMethods);
    // TODO test with an object declaration
    
    value sharedAndDocdMethodsAndAttributes = aInterfaceDecl.annotatedMemberDeclarations<FunctionDeclaration|ValueDeclaration, SharedAnnotation|DocAnnotation>();
    check(! ficd in sharedAndDocdMethodsAndAttributes);
    check(! dicd in sharedAndDocdMethodsAndAttributes);
    check(! iid in sharedAndDocdMethodsAndAttributes);
    check(fa in sharedAndDocdMethodsAndAttributes);
    check(gs in sharedAndDocdMethodsAndAttributes);
    // TODO assert(! ngs in sharedAndDocdMethodsAndAttributes);
    check(fmd in sharedAndDocdMethodsAndAttributes);
    check(dmd in sharedAndDocdMethodsAndAttributes);
    check(fmd in sharedAndDocdMethodsAndAttributes);
    check(md in sharedAndDocdMethodsAndAttributes);
    // TODO assert(nsmd in sharedMethods);
    // TODO test with an object declaration
    
}

@test
shared void checkModuleAndImports() {
    value m = aPackage.container;
    assert(exists moddoc = annotations(docAnnotation, m));
    assert(moddoc.description == "Some module doc");
    
    // module imports
    value deps = m.dependencies;
    check(1 == deps.size);
    assert(exists dep = deps[0]);
    check("check" == dep.name);
    check("0.1" == dep.version);
    assert(exists depdoc = annotations(docAnnotation, dep));
    check(depdoc.description == "Neither deprecated nor optional really, but we want to test ModuleImports");
    check(annotations(optAnnotation, dep) exists);
    check(annotations(deprecatedAnnotation, dep) exists);
    
}

@test
shared void checkPackage() {
    value p = aPackage;
    check(! annotations(sharedAnnotation, p) exists);
    check(! annotations(docAnnotation, p) exists);
    
    // TODO each of these with a toplevel object declaration
    value sharedClasses = p.annotatedMembers<ClassDeclaration, SharedAnnotation>();
    check(aClassDecl in sharedClasses);
    check(aAbstractClassDecl in sharedClasses);
    check(! aInterfaceDecl in sharedClasses); // because it's not a class
    check(! aToplevelAttributeDecl in sharedClasses);
    check(! aToplevelGetterSetterDecl in sharedClasses);
    check(! aToplevelFunctionDecl in sharedClasses);
    
    value sharedInterfaces = p.annotatedMembers<InterfaceDeclaration, SharedAnnotation>();
    check(! aClassDecl in sharedInterfaces);
    check(! aAbstractClassDecl in sharedInterfaces);
    check(aInterfaceDecl in sharedInterfaces);
    check(! aToplevelAttributeDecl in sharedInterfaces);
    check(! aToplevelGetterSetterDecl in sharedInterfaces);
    check(! aToplevelFunctionDecl in sharedInterfaces);
    
    value sharedClassesAndInterfaces = p.annotatedMembers<ClassOrInterfaceDeclaration, SharedAnnotation>();
    check(aClassDecl in sharedClassesAndInterfaces);
    check(aAbstractClassDecl in sharedClassesAndInterfaces);
    check(aInterfaceDecl in sharedClassesAndInterfaces);
    check(! aToplevelAttributeDecl in sharedClassesAndInterfaces);
    check(! aToplevelGetterSetterDecl in sharedClassesAndInterfaces);
    check(! aToplevelFunctionDecl in sharedClassesAndInterfaces);
    
    value sharedAttributes = p.annotatedMembers<ValueDeclaration, SharedAnnotation>();
    check(! aClassDecl in sharedAttributes);
    check(! aAbstractClassDecl in sharedAttributes);
    check(! aInterfaceDecl in sharedAttributes);
    check(aToplevelAttributeDecl in sharedAttributes);
    check(aToplevelGetterSetterDecl in sharedAttributes);
    check(! aToplevelFunctionDecl in sharedAttributes);
    
    value sharedVariables = p.annotatedMembers<VariableDeclaration, SharedAnnotation>();
    check(! aClassDecl in sharedVariables);
    check(! aAbstractClassDecl in sharedVariables);
    check(! aInterfaceDecl in sharedVariables);
    check(! aToplevelAttributeDecl in sharedVariables);
    check(aToplevelGetterSetterDecl in sharedVariables);
    check(! aToplevelFunctionDecl in sharedVariables);
    
    value sharedFunctions = p.annotatedMembers<FunctionDeclaration, SharedAnnotation>();
    check(! aClassDecl in sharedFunctions);
    check(! aAbstractClassDecl in sharedFunctions);
    check(! aInterfaceDecl in sharedFunctions);
    check(! aToplevelAttributeDecl in sharedFunctions);
    check(! aToplevelGetterSetterDecl in sharedFunctions);
    check(aToplevelFunctionDecl in sharedFunctions);
    
    // With a sequenced annotation
    value seqClasses = p.annotatedMembers<ClassDeclaration, Seq>();
    check(aClassDecl in seqClasses);
    check(aAbstractClassDecl in seqClasses);
    check(! aInterfaceDecl in seqClasses); // because it's not a class
    check(! aToplevelAttributeDecl in seqClasses);
    check(! aToplevelGetterSetterDecl in seqClasses);
    check(! aToplevelFunctionDecl in seqClasses);
    
    value seqInterfaces = p.annotatedMembers<InterfaceDeclaration, Seq>();
    check(! aClassDecl in seqInterfaces);
    check(! aAbstractClassDecl in seqInterfaces);
    check(aInterfaceDecl in seqInterfaces);
    check(! aToplevelAttributeDecl in seqInterfaces);
    check(! aToplevelGetterSetterDecl in seqInterfaces);
    check(! aToplevelFunctionDecl in seqInterfaces);
    
    value seqClassesAndInterfaces = p.annotatedMembers<ClassOrInterfaceDeclaration, Seq>();
    check(aClassDecl in seqClassesAndInterfaces);
    check(aAbstractClassDecl in seqClassesAndInterfaces);
    check(aInterfaceDecl in seqClassesAndInterfaces);
    check(! aToplevelAttributeDecl in seqClassesAndInterfaces);
    check(! aToplevelGetterSetterDecl in seqClassesAndInterfaces);
    check(! aToplevelFunctionDecl in seqClassesAndInterfaces);
    
    value seqAttributes = p.annotatedMembers<ValueDeclaration, Seq>();
    check(! aClassDecl in seqAttributes);
    check(! aAbstractClassDecl in seqAttributes);
    check(! aInterfaceDecl in seqAttributes);
    check(aToplevelAttributeDecl in seqAttributes);
    check(aToplevelGetterSetterDecl in seqAttributes);
    check(! aToplevelFunctionDecl in seqAttributes);
    
    value seqVariables = p.annotatedMembers<VariableDeclaration, Seq>();
    check(! aClassDecl in seqVariables);
    check(! aAbstractClassDecl in seqVariables);
    check(! aInterfaceDecl in seqVariables);
    check(! aToplevelAttributeDecl in seqVariables);
    check(aToplevelGetterSetterDecl in seqVariables);
    check(! aToplevelFunctionDecl in seqVariables);
    
    value seqFunctions = p.annotatedMembers<FunctionDeclaration, Seq>();
    check(! aClassDecl in seqFunctions);
    check(! aAbstractClassDecl in seqFunctions);
    check(! aInterfaceDecl in seqFunctions);
    check(! aToplevelAttributeDecl in seqFunctions);
    check(! aToplevelGetterSetterDecl in seqFunctions);
    check(aToplevelFunctionDecl in seqFunctions);
    
    value sharedOrDocdCallables = p.annotatedMembers<FunctionDeclaration|ClassDeclaration, SharedAnnotation|DocAnnotation>();
    check(aClassDecl in sharedOrDocdCallables);
    check(aAbstractClassDecl in sharedOrDocdCallables);
    check(! aInterfaceDecl in sharedOrDocdCallables);
    check(! aToplevelAttributeDecl in sharedOrDocdCallables);
    check(! aToplevelGetterSetterDecl in sharedOrDocdCallables);
    check(aToplevelFunctionDecl in sharedOrDocdCallables);
    
    value abstractCallables = p.annotatedMembers<FunctionDeclaration|ClassDeclaration, AbstractAnnotation>();
    check(! aClassDecl in abstractCallables);
    check(aAbstractClassDecl in abstractCallables);
    check(! aInterfaceDecl in abstractCallables);
    check(! aToplevelAttributeDecl in abstractCallables);
    check(! aToplevelGetterSetterDecl in abstractCallables);
    check(! aToplevelFunctionDecl in abstractCallables);
    
    value sharedDeclarations = p.annotatedMembers<NestableDeclaration, SharedAnnotation>();
    check(aClassDecl in sharedDeclarations);
    check(aAbstractClassDecl in sharedDeclarations);
    check(aInterfaceDecl in sharedDeclarations);
    check(aToplevelAttributeDecl in sharedDeclarations);
    check(aToplevelGetterSetterDecl in sharedDeclarations);
    check(aToplevelFunctionDecl in sharedDeclarations);

}

@test
shared void checkMetamodelRefs() {
    value decls = annotations(`Decl`, `class MetamodelRefs`);
    
    assert(exists d0 = decls[0],
        d0.decl is ValueDeclaration,
        `value aToplevelAttribute` == d0.decl);
    
    assert(exists d1 = decls[1],
        d1.decl is ValueDeclaration,
        `value aToplevelGetterSetter` == d1.decl);
    
    assert(exists d2 = decls[2],
        d2.decl is FunctionDeclaration,
        `function aToplevelFunction` == d2.decl);
    
    assert(exists d3 = decls[3],
        d3.decl is ValueDeclaration,
        `value aToplevelObject` == d3.decl);
    
    assert(exists d4 = decls[4],
        d4.decl is InterfaceDeclaration,
        `interface AInterface` == d4.decl);
    
    assert(exists d5 = decls[5],
        d5.decl is FunctionDeclaration,
        `function AInterface.FormalInnerClass.method` == d5.decl);
    
    assert(exists d6 = decls[6],
        d6.decl is ClassDeclaration,
        `class AInterface.DefaultInnerClass` == d6.decl);
    
    assert(exists d7 = decls[7],
        d7.decl is FunctionDeclaration,
        `function AInterface.DefaultInnerClass.method` == d7.decl);
    
    assert(exists d8 = decls[8],
        d8.decl is InterfaceDeclaration,
        `interface AInterface.SharedInnerInterface` == d8.decl);
    
    assert(exists d9 = decls[9],
        d9.decl is FunctionDeclaration,
        `function AInterface.SharedInnerInterface.method` == d9.decl);
    
    assert(exists d10 = decls[10],
        d10.decl is ValueDeclaration,
        `value AInterface.formalAttribute` == d10.decl);
    
    assert(exists d11 = decls[11],
        d11.decl is ValueDeclaration,
        `value AInterface.defaultGetterSetter` == d11.decl);
    
    assert(exists d12 = decls[12],
        d12.decl is ValueDeclaration,
        `value AInterface.getterSetter` == d12.decl);
    
//Illegal decl(`AInterface.nonsharedGetterSetter`)

    assert(exists d13 = decls[13],
        d13.decl is FunctionDeclaration,
        `function AInterface.formalMethod` == d13.decl);
    
    assert(exists d14 = decls[14],
        d14.decl is FunctionDeclaration,
        `function AInterface.defaultMethod` == d14.decl);
    
    assert(exists d15 = decls[15],
        d15.decl is FunctionDeclaration,
        `function AInterface.method` == d15.decl);
    
//Illegal decl(`AInterface.nonsharedMethod`)

    assert(exists d16 = decls[16],
        d16.decl is ClassDeclaration,
        `class AAbstractClass` == d16.decl);
    
    assert(exists d17 = decls[17],
        d17.decl is ClassDeclaration,
        `class AAbstractClass.FormalInnerClass` == d17.decl);
    
    assert(exists d18 = decls[18],
        d18.decl is ClassDeclaration,
        `class AAbstractClass.DefaultInnerClass` == d18.decl);
    
    assert(exists d19 = decls[19],
        d19.decl is ValueDeclaration,
        `value AAbstractClass.formalAttribute` == d19.decl);
    
    assert(exists d20 = decls[20],
        d20.decl is FunctionDeclaration,
        `function AAbstractClass.formalMethod` == d20.decl);
    
    assert(exists d21 = decls[21],
        d21.decl is ValueDeclaration,
        `value AAbstractClass.objectMember` == d21.decl);

    assert(exists d22 = decls[22],
        d22.decl is ClassDeclaration,
        `class AAbstractClass.InnerClass` == d22.decl);
    
    assert(exists d23 = decls[23],
        d23.decl is FunctionDeclaration,
        `function AAbstractClass.InnerClass.method` == d23.decl);
    
    assert(exists d24 = decls[24],
        d24.decl is InterfaceDeclaration,
        `interface AAbstractClass.InnerInterface` == d24.decl);
    
    assert(exists d25 = decls[25],
        d25.decl is FunctionDeclaration,
        `function AAbstractClass.InnerInterface.method` == d25.decl);
    
    assert(exists d26 = decls[26],
        d26.decl is ClassDeclaration,
        `class AClass` == d26.decl);
    
    assert(exists d27 = decls[27],
        d27.decl is ClassDeclaration,
        `class MetamodelRefs` == d27.decl);
    
    assert(exists d28 = decls[28],
        d28.decl is ValueDeclaration,
        `value MetamodelRefs.parameter` == d28.decl);
    
    assert(exists d29 = decls[29],
        d29.decl is ClassDeclaration,
        `class AClassAlias` == d29.decl);
}

@test
shared void checkAClassAlias() {
    assert(exists acadoc = annotations(docAnnotation, `class AClassAlias`),
            acadoc.description == "AClassAlias");
    
    assert(
            exists acapDecl = `class AClassAlias`.parameterDeclarations[0],
            exists acapdoc = annotations(docAnnotation, acapDecl),
            acapdoc.description == "AClassAlias.p");
    
}

shared void run() {
    checkAToplevelAttributeAnnotations();
    checkAToplevelGetterSetterAnnotations();
    checkAToplevelFunctionAnnotations();
    checkAToplevelObjectAnnotations();
    checkAInterface();
    checkAAbstractClass();
    checkAClass();
    checkAClassAlias();
    // TODO Local declarations
    checkModuleAndImports();
    checkPackage();
    checkMetamodelRefs();
    
    // ATTENTION!
    // When you add new test methods here make sure they are "shared" and marked "@test"!
    
    print("Annotation tests OK");
    results();
}
shared void test() { run(); }
