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
    AliasDeclaration,
    ValueDeclaration,
    NestableDeclaration,
    FunctionDeclaration,
    FunctionOrValueDeclaration,
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
ValueDeclaration aToplevelGetterSetterDecl {
    assert(is ValueDeclaration result = aPackage.getValue("aToplevelGetterSetter"));
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
    check(annotations(sharedAnnotation, aToplevelAttributeDecl) exists, "toplevel attrib 1");
    check(optionalAnnotation(sharedAnnotation, aToplevelAttributeDecl) exists, "toplevel attrib 2");
    check(aToplevelAttributeDecl.annotations<SharedAnnotation>().size == 1, "toplevel attrib 3");
    // doc
    if (exists doc = annotations(docAnnotation, aToplevelAttributeDecl), 
        doc.description == "aToplevelAttribute"){}else{fail("aToplevelAttributeDecl doc == 'aToplevelAttribute' 1");}
    if (exists doc2 = optionalAnnotation(docAnnotation, aToplevelAttributeDecl), 
        doc2.description == "aToplevelAttribute"){}else{fail("aToplevelAttributeDecl doc=='aToplevelAttribute' 2");}
    if (nonempty doc3 = aToplevelAttributeDecl.annotations<DocAnnotation>(),
        doc3.first.description == "aToplevelAttribute"){}else{fail("aToplevelAttributeDecl doc='aToplevelAttribute' 3");}
    // seq
    variable value seqs = annotations(seqAnnotation, aToplevelAttributeDecl);
    check(seqs.size == 2, "toplevel attrib 4");
    assert(exists seq = seqs[0], seq.seq == "aToplevelAttribute 1");
    assert(exists seq2 = seqs[1], seq2.seq == "aToplevelAttribute 2");
    check(sequencedAnnotations(seqAnnotation, aToplevelAttributeDecl).size == 2, "toplevel attrib 5");
    check(aToplevelAttributeDecl.annotations<Seq>().size == 2, "toplevel attrib 6");
    
    // Using funky type arguments to Declaration.annotations<>()
    check(aToplevelAttributeDecl.annotations<Nothing>().empty, "toplevel attrib 7");
    // TODO Depends on fix for #1157 assert(aToplevelAttributeDecl.annotations<ConstrainedAnnotation<Nothing, Anything, ValueDeclaration>>() empty);
    assert(nonempty doc4 = aToplevelAttributeDecl.annotations<ConstrainedAnnotation<DocAnnotation, Anything, ValueDeclaration>>(),
        is DocAnnotation doc4_1 = doc4.first,
        doc4_1.description == "aToplevelAttribute");
    assert(nonempty doc5 = aToplevelAttributeDecl.annotations<OptionalAnnotation<DocAnnotation, ValueDeclaration>>(),
        is DocAnnotation doc5_1 = doc5.first,
        doc5_1.description == "aToplevelAttribute");
    check(aToplevelAttributeDecl.annotations<SharedAnnotation|DocAnnotation|Seq>().size == 4, "toplevel attrib 8");
    check(aToplevelAttributeDecl.annotations<SharedAnnotation|DocAnnotation>().size == 2, "toplevel attrib 9");
    check(aToplevelAttributeDecl.annotations<DocAnnotation|Seq>().size == 3, "toplevel attrib 10");
    
    // since DocAnnotation is not Sequenced, this returns empty:
    assert(nonempty shared6 = aToplevelAttributeDecl.annotations<OptionalAnnotation<SharedAnnotation, ValueDeclaration>>(),
        is SharedAnnotation shared6_1 = shared6.first);
    assert(nonempty seq7 = aToplevelAttributeDecl.annotations<SequencedAnnotation<Seq, ValueDeclaration>>(),
        is Seq seq7_1 = seq7.first,
        seq7_1.seq == "aToplevelAttribute 1");
    
    assert(nonempty see = annotations(seeAnnotation, aToplevelAttributeDecl));
    check(see.size == 1, "toplevel attrib 11");
    check(see.first.programElements.size == 3, "toplevel attrib 12");
    check(see.first.programElements.contains(`value aToplevelGetterSetter`), "toplevel attrib 13");
    check(see.first.programElements.contains(`module ceylon.language`), "toplevel attrib 14");
    check(see.first.programElements.contains(`package ceylon.language.meta.declaration`), "toplevel attrib 15");
    
    
    assert(exists enumed = annotations(enumeratedAnnotation, aToplevelAttributeDecl));
    check(enumed.c == larger, "toplevel attrib 16");
    
    assert(exists enumedVariadic = annotations(enumeratedVariadicAnnotation, aToplevelAttributeDecl));
    check(enumedVariadic.c.size == 3, "toplevel attrib 17");
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
    check(annotations(sharedAnnotation, aToplevelGetterSetterDecl) exists, "toplevel getter/setter 1");
    check(optionalAnnotation(sharedAnnotation, aToplevelGetterSetterDecl) exists, "toplevel getter/setter 2");
    check(aToplevelGetterSetterDecl.annotations<SharedAnnotation>() nonempty, "toplevel getter/setter 3");
    // doc
    assert(exists doc = annotations(docAnnotation, aToplevelGetterSetterDecl), 
        doc.description == "aToplevelGetter");
    assert(exists doc2 = optionalAnnotation(docAnnotation, aToplevelGetterSetterDecl), 
        doc2.description == "aToplevelGetter");
    assert(nonempty doc3 = aToplevelGetterSetterDecl.annotations<DocAnnotation>(),
        doc3.first.description == "aToplevelGetter");
    // seq
    variable value seqs = annotations(seqAnnotation, aToplevelGetterSetterDecl);
    check(seqs.size == 1, "toplevel getter/setter 4");
    assert(exists seq = seqs[0], seq.seq == "aToplevelGetter 1");
    check(sequencedAnnotations(seqAnnotation, aToplevelGetterSetterDecl).size == 1, "toplevel getter/setter 5");
    assert(nonempty seq2 = aToplevelGetterSetterDecl.annotations<Seq>(),
        seq2.first.seq == "aToplevelGetter 1");
    
    // setter
    assert(exists setter = aToplevelGetterSetterDecl.setter,
        exists docsetter = optionalAnnotation(docAnnotation, setter), 
        docsetter.description == "aToplevelSetter");
    
    assert(nonempty see = annotations(seeAnnotation, aToplevelGetterSetterDecl));
    check(see.size == 1, "toplevel getter/setter 6");
    check(see.first.programElements.contains(`value aToplevelAttribute`), "toplevel getter/setter 7");
}

@test
shared void checkAToplevelFunctionAnnotations() {
    //shared
    check(annotations(sharedAnnotation, aToplevelFunctionDecl) exists, "toplevel func 1");
    check(optionalAnnotation(sharedAnnotation, aToplevelFunctionDecl) exists, "toplevel func 2");
    check(aToplevelFunctionDecl.annotations<SharedAnnotation>() nonempty, "toplevel func 3");
    // doc
    assert(exists doc = annotations(docAnnotation, aToplevelFunctionDecl), 
            doc.description == "aToplevelFunction");
    assert(exists doc2 = optionalAnnotation(docAnnotation, aToplevelFunctionDecl), 
            doc2.description == "aToplevelFunction");
    assert(nonempty doc3=aToplevelFunctionDecl.annotations<DocAnnotation>(),
            doc3.first.description == "aToplevelFunction");
    // seq
    variable value seqs = annotations(seqAnnotation, aToplevelFunctionDecl);
    check(seqs.size == 1, "toplevel func 4");
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
    check(pseqs.size == 1, "toplevel func 5");
    assert(exists pseq = pseqs[0],
            pseq.seq== "aToplevelFunction.parameter 1");
}

@test
shared void checkAToplevelObjectAnnotations() {
    
    //shared
    check(annotations(sharedAnnotation, aToplevelObjectDecl) exists, "toplevel obj 1");
    check(optionalAnnotation(sharedAnnotation, aToplevelObjectDecl) exists, "toplevel obj 2");
    check(aToplevelObjectDecl.annotations<SharedAnnotation>() nonempty, "toplevel obj 3");
    
    // doc
    assert(exists doc = annotations(docAnnotation, aToplevelObjectDecl), 
        doc.description == "aToplevelObject");
    assert(exists doc2 = optionalAnnotation(docAnnotation, aToplevelObjectDecl), 
        doc2.description == "aToplevelObject");
    assert(nonempty doc3 = aToplevelObjectDecl.annotations<DocAnnotation>(),
        doc3.first.description == "aToplevelObject");
    // seq
    variable value seqs = annotations(seqAnnotation, aToplevelObjectDecl);
    check(seqs.size == 1, "toplevel obj 4");
    assert(exists seq = seqs[0], seq.seq == "aToplevelObject 1");
    check(sequencedAnnotations(seqAnnotation, aToplevelObjectDecl).size == 1, "toplevel obj 5");
    assert(nonempty seq2 = aToplevelObjectDecl.annotations<Seq>(),
        seq2.first.seq == "aToplevelObject 1");
}

@test
shared void checkAClass() {
    //shared
    check(annotations(sharedAnnotation, aClassDecl) exists, "class 1");
    check(optionalAnnotation(sharedAnnotation, aClassDecl) exists, "class 2");
    check(aClassDecl.annotations<SharedAnnotation>() nonempty, "class 3");
    //abstract
    check(! annotations(abstractAnnotation, aClassDecl) exists, "class 4");
    check(! optionalAnnotation(abstractAnnotation, aClassDecl) exists, "class 5");
    check(! aClassDecl.annotations<AbstractAnnotation>() nonempty, "class 6");
    // doc
    assert(exists doc = annotations(docAnnotation, aClassDecl), 
        doc.description == "AClass");
    assert(exists doc2 = optionalAnnotation(docAnnotation, aClassDecl), 
        doc2.description == "AClass");
    assert(nonempty doc3 = aClassDecl.annotations<DocAnnotation>(),
        doc3.first.description == "AClass");
    // seq
    variable value seqs = annotations(seqAnnotation, aClassDecl);
    check(seqs.size == 2, "class 7");
    assert(exists seq = seqs[0], seq.seq == "AClass 1");
    assert(exists seq2 = seqs[1], seq2.seq == "AClass 2");
    check(sequencedAnnotations(seqAnnotation, aClassDecl).size == 2, "class 8");
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
    check(pseqs.size == 2, "class 9");
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
    check(annotations(sharedAnnotation, aAbstractClassDecl) exists, "abstract class 1");
    check(optionalAnnotation(sharedAnnotation, aAbstractClassDecl) exists, "abstract class 2");
    //abstract
    check(annotations(abstractAnnotation, aAbstractClassDecl) exists, "abstract class 3");
    check(optionalAnnotation(abstractAnnotation, aAbstractClassDecl) exists, "abstract class 4");
    // doc
    assert(exists doc = annotations(docAnnotation, aAbstractClassDecl), 
        doc.description == "AAbstractClass");
    assert(exists doc2 = optionalAnnotation(docAnnotation, aAbstractClassDecl), 
        doc2.description == "AAbstractClass");
    // seq
    variable value seqs = annotations(seqAnnotation, aAbstractClassDecl);
    check(seqs.size == 2, "abstract class 5");
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
    check(pseqs.size == 0, "abstract class 6");
    
    // Members of abstract class
    // formalAttribute
    assert(exists fam=aAbstractClassDecl.apply<AAbstractClass>().getAttribute<AAbstractClass, String>("formalAttribute"));
    ValueDeclaration fa=fam(AClass("")).declaration;
    check(annotations(sharedAnnotation, fa) exists, "abstract class 7");
    check(annotations(actualAnnotation, fa) exists, "abstract class 8");
    assert(exists fadoc = annotations(docAnnotation, fa),
            fadoc.description == "AAbstractClass.formalAttributeGetter");
    assert(exists fasetter = fa.setter,
            exists fasdoc = annotations(docAnnotation, fasetter),
            fasdoc.description == "AAbstractClass.formalAttributeSetter");
    
    // formalMethod
    assert(exists fmm=aAbstractClassDecl.apply<AAbstractClass>().getMethod<AAbstractClass, Anything, [String]>("formalMethod"));
    value fm=fmm(AClass("")).declaration;
    // shared
    check(annotations(sharedAnnotation, fm) exists, "abstract class 9");
    // actual
    check(annotations(actualAnnotation, fm) exists, "abstract class 10");
    // default
    check(annotations(defaultAnnotation, fm) exists, "abstract class 11");
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
    check(annotations(sharedAnnotation, ic) exists, "abstract class 12");
    // shared
    assert(exists icdoc = annotations(docAnnotation, ic));
    check(icdoc.description == "AAbstractClass.InnerClass", "abstract class 13");
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
    check(annotations(sharedAnnotation, ii) exists, "abstract class 14");
    // shared
    assert(exists iidoc = annotations(docAnnotation, ii));
    check(iidoc.description == "AAbstractClass.InnerInterface", "abstract class 15");
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
    check(annotations(sharedAnnotation, aInterfaceDecl) exists, "iface 1");
    check(optionalAnnotation(sharedAnnotation, aInterfaceDecl) exists, "iface 2");
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
    check(seqs.size == 2, "iface 3");
    assert(exists seq = seqs[0], 
            seq.seq == "AInterface 1");
    assert(exists seq2 = seqs[1], 
            seq2.seq == "AInterface 2");
    check(sequencedAnnotations(seqAnnotation, aInterfaceDecl).size == 2, "iface 4");
    
    // Members of interface
    // formalAttribute
    assert(exists fam=iface.getAttribute<AInterface, String>("formalAttribute"));
    value fa = fam(AClass("")).declaration;
    check(annotations(sharedAnnotation, fa) exists, "iface 5");
    assert(exists fadoc = annotations(docAnnotation, fa),
            fadoc.description == "AInterface.formalAttribute");
    
    // defaultGetterSetter
    assert(exists dgsm=iface.getAttribute<AInterface, String>("defaultGetterSetter"));
    ValueDeclaration dgs = dgsm(AClass("")).declaration;
    check(annotations(sharedAnnotation, dgs) exists, "iface 6");
    check(annotations(defaultAnnotation, dgs) exists, "iface 7");
    assert(exists dgdoc = annotations(docAnnotation, dgs),
            dgdoc.description == "AInterface.defaultGetter");
    assert(exists dgssetter = dgs.setter,
            exists dsdoc = annotations(docAnnotation, dgssetter),
            dsdoc.description == "AInterface.defaultSetter");
    
    // getterSetter
    assert(exists gsm=iface.getAttribute<AInterface, String>("getterSetter"));
    ValueDeclaration gs = gsm(AClass("")).declaration;
    check(annotations(sharedAnnotation, gs) exists, "iface 8");
    assert(exists gsdoc = annotations(docAnnotation, gs),
            gsdoc.description == "AInterface.getter");
    // setter annotations
    assert(exists gssetter = gs.setter,
            exists gssdoc = annotations(docAnnotation, gssetter),
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
    check(ficd in sharedClasses, "iface 9");
    check(dicd in sharedClasses, "iface 10");
    check(! iid in sharedClasses, "iface 11");
    check(! fa in sharedClasses, "iface 12");
    check(! gs in sharedClasses, "iface 13");
    // TODO assert(! ngs in sharedClasses);
    check(! fmd in sharedClasses, "iface 14");
    check(! dmd in sharedClasses, "iface 15");
    check(! fmd in sharedClasses, "iface 16");
    check(! md in sharedClasses, "iface 17");
    // TODO assert(! nsmd in sharedClasses);
    // TODO test with an object declaration
    
    value sharedInterfaces = aInterfaceDecl.annotatedMemberDeclarations<InterfaceDeclaration, SharedAnnotation>();
    check(!ficd in sharedInterfaces, "iface 18");
    check(!dicd in sharedInterfaces, "iface 19");
    check(iid in sharedInterfaces, "iface 20");
    check(! fa in sharedInterfaces, "iface 21");
    check(! gs in sharedInterfaces, "iface 22");
    // TODO assert(! ngs in sharedInterfaces);
    check(! fmd in sharedInterfaces, "iface 23");
    check(! dmd in sharedInterfaces, "iface 24");
    check(! fmd in sharedInterfaces, "iface 25");
    check(! md in sharedInterfaces, "iface 26");
    // TODO assert(! nsmd in sharedInterfaces);
    // TODO test with an object declaration
    
    value sharedClassesAndInterfaces = aInterfaceDecl.annotatedMemberDeclarations<ClassOrInterfaceDeclaration, SharedAnnotation>();
    check(ficd in sharedClassesAndInterfaces, "iface 27");
    check(dicd in sharedClassesAndInterfaces, "iface 28");
    check(iid in sharedClassesAndInterfaces, "iface 29");
    check(! fa in sharedClassesAndInterfaces, "iface 30");
    check(! gs in sharedClassesAndInterfaces, "iface 31");
    // TODO assert(! ngs in sharedClassesAndInterfaces);
    check(! fmd in sharedClassesAndInterfaces, "iface 32");
    check(! dmd in sharedClassesAndInterfaces, "iface 33");
    check(! fmd in sharedClassesAndInterfaces, "iface 34");
    check(! md in sharedClassesAndInterfaces, "iface 35");
    // TODO assert(! nsmd in sharedInterfaces);
    // TODO test with an object declaration
    
    value sharedAttributes = aInterfaceDecl.annotatedMemberDeclarations<ValueDeclaration, SharedAnnotation>();
    check(! ficd in sharedAttributes, "iface 36");
    check(! dicd in sharedAttributes, "iface 37");
    check(! iid in sharedAttributes, "iface 38");
    check(fa in sharedAttributes, "iface 39");
    check(gs in sharedAttributes, "iface 40");
    // TODO assert(ngs in sharedAttributes);
    check(! fmd in sharedAttributes, "iface 41");
    check(! dmd in sharedAttributes, "iface 42");
    check(! fmd in sharedAttributes, "iface 43");
    check(! md in sharedAttributes, "iface 44");
    // TODO assert(! nsmd in sharedAttribute);
    // TODO test with an object declaration
    
    value sharedMethods = aInterfaceDecl.annotatedMemberDeclarations<FunctionDeclaration, SharedAnnotation>();
    check(! ficd in sharedMethods, "iface 45");
    check(! dicd in sharedMethods, "iface 46");
    check(! iid in sharedMethods, "iface 47");
    check(! fa in sharedMethods, "iface 48");
    check(! gs in sharedMethods, "iface 49");
    // TODO assert(! ngs in sharedMethods);
    check(fmd in sharedMethods, "iface 50");
    check(dmd in sharedMethods, "iface 51");
    check(fmd in sharedMethods, "iface 52");
    check(md in sharedMethods, "iface 53");
    // TODO assert(nsmd in sharedMethods);
    // TODO test with an object declaration
    
    value sharedAndDocdMethodsAndAttributes = aInterfaceDecl.annotatedMemberDeclarations<FunctionDeclaration|ValueDeclaration, SharedAnnotation|DocAnnotation>();
    check(! ficd in sharedAndDocdMethodsAndAttributes, "iface 54");
    check(! dicd in sharedAndDocdMethodsAndAttributes, "iface 55");
    check(! iid in sharedAndDocdMethodsAndAttributes, "iface 56");
    check(fa in sharedAndDocdMethodsAndAttributes, "iface 57");
    check(gs in sharedAndDocdMethodsAndAttributes, "iface 58");
    // TODO assert(! ngs in sharedAndDocdMethodsAndAttributes);
    check(fmd in sharedAndDocdMethodsAndAttributes, "iface 59");
    check(dmd in sharedAndDocdMethodsAndAttributes, "iface 60");
    check(fmd in sharedAndDocdMethodsAndAttributes, "iface 61");
    check(md in sharedAndDocdMethodsAndAttributes, "iface 62");
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
    check(1 == deps.size, "module 1");
    assert(exists dep = deps[0]);
    check("check" == dep.name, "module 2");
    check("0.1" == dep.version, "module 3");
    if (exists depdoc = annotations(docAnnotation, dep)) {
      check(depdoc.description == "Neither deprecated nor optional really, but we want to test ModuleImports", "module 4");
    } else {
      fail("module 4");
    }
    check(annotations(optAnnotation, dep) exists, "module 5");
    check(annotations(deprecatedAnnotation, dep) exists, "module 6");
    
}

@test
shared void checkPackage() {
    value p = aPackage;
    check(! annotations(sharedAnnotation, p) exists, "pkg 1");
    check(! annotations(docAnnotation, p) exists, "pkg 2");
    
    // TODO each of these with a toplevel object declaration
    value sharedClasses = p.annotatedMembers<ClassDeclaration, SharedAnnotation>();
    check(aClassDecl in sharedClasses, "pkg 3");
    check(aAbstractClassDecl in sharedClasses, "pkg 4");
    check(! aInterfaceDecl in sharedClasses, "pkg 5"); // because it's not a class
    check(! aToplevelAttributeDecl in sharedClasses, "pkg 6");
    check(! aToplevelGetterSetterDecl in sharedClasses, "pkg 7");
    check(! aToplevelFunctionDecl in sharedClasses, "pkg 8");
    
    value sharedInterfaces = p.annotatedMembers<InterfaceDeclaration, SharedAnnotation>();
    check(! aClassDecl in sharedInterfaces, "pkg 9");
    check(! aAbstractClassDecl in sharedInterfaces, "pkg 10");
    check(aInterfaceDecl in sharedInterfaces, "pkg 11");
    check(! aToplevelAttributeDecl in sharedInterfaces, "pkg 12");
    check(! aToplevelGetterSetterDecl in sharedInterfaces, "pkg 13");
    check(! aToplevelFunctionDecl in sharedInterfaces, "pkg 14");
    
    value sharedClassesAndInterfaces = p.annotatedMembers<ClassOrInterfaceDeclaration, SharedAnnotation>();
    check(aClassDecl in sharedClassesAndInterfaces, "pkg 15");
    check(aAbstractClassDecl in sharedClassesAndInterfaces, "pkg 16");
    check(aInterfaceDecl in sharedClassesAndInterfaces, "pkg 17");
    check(! aToplevelAttributeDecl in sharedClassesAndInterfaces, "pkg 18");
    check(! aToplevelGetterSetterDecl in sharedClassesAndInterfaces, "pkg 19");
    check(! aToplevelFunctionDecl in sharedClassesAndInterfaces, "pkg 20");
    
    value sharedAttributes = p.annotatedMembers<ValueDeclaration, SharedAnnotation>();
    check(! aClassDecl in sharedAttributes, "pkg 21");
    check(! aAbstractClassDecl in sharedAttributes, "pkg 22");
    check(! aInterfaceDecl in sharedAttributes, "pkg 23");
    check(aToplevelAttributeDecl in sharedAttributes, "pkg 24");
    check(aToplevelGetterSetterDecl in sharedAttributes, "pkg 25");
    check(! aToplevelFunctionDecl in sharedAttributes, "pkg 26");
    
    value sharedFunctions = p.annotatedMembers<FunctionDeclaration, SharedAnnotation>();
    check(! aClassDecl in sharedFunctions, "pkg 27");
    check(! aAbstractClassDecl in sharedFunctions, "pkg 28");
    check(! aInterfaceDecl in sharedFunctions, "pkg 29");
    check(! aToplevelAttributeDecl in sharedFunctions, "pkg 30");
    check(! aToplevelGetterSetterDecl in sharedFunctions, "pkg 31");
    check(aToplevelFunctionDecl in sharedFunctions, "pkg 32");
    
    // With a sequenced annotation
    value seqClasses = p.annotatedMembers<ClassDeclaration, Seq>();
    check(aClassDecl in seqClasses, "pkg 33");
    check(aAbstractClassDecl in seqClasses, "pkg 34");
    check(! aInterfaceDecl in seqClasses, "pkg 35"); // because it's not a class
    check(! aToplevelAttributeDecl in seqClasses, "pkg 36");
    check(! aToplevelGetterSetterDecl in seqClasses, "pkg 37");
    check(! aToplevelFunctionDecl in seqClasses, "pkg 38");
    
    value seqInterfaces = p.annotatedMembers<InterfaceDeclaration, Seq>();
    check(! aClassDecl in seqInterfaces, "pkg 39");
    check(! aAbstractClassDecl in seqInterfaces, "pkg 40");
    check(aInterfaceDecl in seqInterfaces, "pkg 41");
    check(! aToplevelAttributeDecl in seqInterfaces, "pkg 42");
    check(! aToplevelGetterSetterDecl in seqInterfaces, "pkg 43");
    check(! aToplevelFunctionDecl in seqInterfaces, "pkg 44");
    
    value seqClassesAndInterfaces = p.annotatedMembers<ClassOrInterfaceDeclaration, Seq>();
    check(aClassDecl in seqClassesAndInterfaces, "pkg 45");
    check(aAbstractClassDecl in seqClassesAndInterfaces, "pkg 46");
    check(aInterfaceDecl in seqClassesAndInterfaces, "pkg 47");
    check(! aToplevelAttributeDecl in seqClassesAndInterfaces, "pkg 48");
    check(! aToplevelGetterSetterDecl in seqClassesAndInterfaces, "pkg 49");
    check(! aToplevelFunctionDecl in seqClassesAndInterfaces, "pkg 50");
    
    value seqAttributes = p.annotatedMembers<ValueDeclaration, Seq>();
    check(! aClassDecl in seqAttributes, "pkg 51");
    check(! aAbstractClassDecl in seqAttributes, "pkg 52");
    check(! aInterfaceDecl in seqAttributes, "pkg 53");
    check(aToplevelAttributeDecl in seqAttributes, "pkg 54");
    check(aToplevelGetterSetterDecl in seqAttributes, "pkg 55");
    check(! aToplevelFunctionDecl in seqAttributes, "pkg 56");
    
    value seqFunctions = p.annotatedMembers<FunctionDeclaration, Seq>();
    check(! aClassDecl in seqFunctions, "pkg 57");
    check(! aAbstractClassDecl in seqFunctions, "pkg 58");
    check(! aInterfaceDecl in seqFunctions, "pkg 59");
    check(! aToplevelAttributeDecl in seqFunctions, "pkg 60");
    check(! aToplevelGetterSetterDecl in seqFunctions, "pkg 61");
    check(aToplevelFunctionDecl in seqFunctions, "pkg 62");
    
    value sharedOrDocdCallables = p.annotatedMembers<FunctionDeclaration|ClassDeclaration, SharedAnnotation|DocAnnotation>();
    check(aClassDecl in sharedOrDocdCallables, "pkg 63");
    check(aAbstractClassDecl in sharedOrDocdCallables, "pkg 64");
    check(! aInterfaceDecl in sharedOrDocdCallables, "pkg 65");
    check(! aToplevelAttributeDecl in sharedOrDocdCallables, "pkg 66");
    check(! aToplevelGetterSetterDecl in sharedOrDocdCallables, "pkg 67");
    check(aToplevelFunctionDecl in sharedOrDocdCallables, "pkg 68");
    
    value abstractCallables = p.annotatedMembers<FunctionDeclaration|ClassDeclaration, AbstractAnnotation>();
    check(! aClassDecl in abstractCallables, "pkg 69");
    check(aAbstractClassDecl in abstractCallables, "pkg 70");
    check(! aInterfaceDecl in abstractCallables, "pkg 71");
    check(! aToplevelAttributeDecl in abstractCallables, "pkg 72");
    check(! aToplevelGetterSetterDecl in abstractCallables, "pkg 73");
    check(! aToplevelFunctionDecl in abstractCallables, "pkg 74");
    
    value sharedDeclarations = p.annotatedMembers<NestableDeclaration, SharedAnnotation>();
    check(aClassDecl in sharedDeclarations, "pkg 75");
    check(aAbstractClassDecl in sharedDeclarations, "pkg 76");
    check(aInterfaceDecl in sharedDeclarations, "pkg 77");
    check(aToplevelAttributeDecl in sharedDeclarations, "pkg 78");
    check(aToplevelGetterSetterDecl in sharedDeclarations, "pkg 79");
    check(aToplevelFunctionDecl in sharedDeclarations, "pkg 80");
    
    value sharedFunctionsOrValues = p.annotatedMembers<FunctionOrValueDeclaration, SharedAnnotation>();
    check(! aClassDecl in sharedFunctionsOrValues, "pkg 81");
    check(! aAbstractClassDecl in sharedFunctionsOrValues, "pkg 82");
    check(! aInterfaceDecl in sharedFunctionsOrValues, "pkg 83");
    check(aToplevelAttributeDecl in sharedFunctionsOrValues, "pkg 84");
    check(aToplevelGetterSetterDecl in sharedFunctionsOrValues, "pkg 85");
    check(aToplevelFunctionDecl in sharedFunctionsOrValues, "pkg 86");
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

    assert(exists d30 = decls[30],
        d30.decl is AliasDeclaration,
        `alias Alias` == d30.decl);

    assert(exists d31 = decls[31],
        d31.decl is AliasDeclaration,
        `alias AAbstractClass.Alias` == d31.decl);
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

    testBug378();
    bug409();
        
    print("Annotation tests OK");
    results();
}
shared void test() { run(); }
