import ceylon.language.meta.declaration{Declaration}

shared final annotation class Seq(shared String seq) 
    satisfies SequencedAnnotation<Seq, Annotated> {}
shared annotation Seq seq(String s) => Seq(s); 

shared final annotation class Decl(shared Declaration decl)
    satisfies SequencedAnnotation<Decl, Annotated> {}
shared annotation Decl decl(Declaration d) => Decl(d);

shared final annotation class Enumerated(shared Comparison c)
    satisfies OptionalAnnotation<Enumerated, Annotated> {}
shared annotation Enumerated enumerated(Comparison c) => Enumerated(c);

shared final annotation class EnumeratedVariadic(shared Comparison* c)
    satisfies OptionalAnnotation<EnumeratedVariadic, Annotated> {}
shared annotation EnumeratedVariadic enumeratedVariadic(Comparison* c) => EnumeratedVariadic(*c);

"aToplevelAttribute"
seq("aToplevelAttribute 1")
seq("aToplevelAttribute 2")
see(`value aToplevelGetterSetter`,
    `module ceylon.language`,
    `package ceylon.language.meta.declaration`)
enumerated(larger)
enumeratedVariadic(larger, equal, smaller)
shared String aToplevelAttribute = "";

"aToplevelGetter"
seq("aToplevelGetter 1")
see(`value aToplevelAttribute`)
shared String aToplevelGetterSetter {
    return "";
}
"aToplevelSetter"
assign aToplevelGetterSetter {
}
"aToplevelFunction"
seq("aToplevelFunction 1")
shared void aToplevelFunction(
    "aToplevelFunction.parameter"
    seq("aToplevelFunction.parameter 1")
    String parameter) {
    
}
"aToplevelObject"
seq("aToplevelObject 1")
shared object aToplevelObject {
}

"AInterface"
seq("AInterface 1")
seq("AInterface 2")
shared interface AInterface {
    
    "AInterface.FormalInnerClass"
    shared formal class FormalInnerClass(
            "AInterface.FormalInnerClass.parameter"
            String parameter) {
        "AInterface.FormalInnerClass.method"
        shared void method(
                "AInterface.FormalInnerClass.method.parameter"
                String parameter) {}
    }
    
    "AInterface.DefaultInnerClass"
    shared default class DefaultInnerClass(
            "AInterface.DefaultInnerClass.parameter"
            String parameter) {
        
        "AInterface.DefaultInnerClass.method"
        shared void method(
                "Interface.DefaultInnerClass.method.parameter"
                String parameter) {}
    }
    
    "AInterface.SharedInnerInterface"
    shared interface SharedInnerInterface {
        "AInterface.SharedInnerInterface.method"
        shared void method(
                "Interface.SharedInnerInterface.method.parameter"
                String parameter) {}
    }
    
    "AInterface.formalAttribute"
    shared formal String formalAttribute;
    
    "AInterface.defaultGetter"
    shared default String defaultGetterSetter {
        return "";
    }
    "AInterface.defaultSetter"
    assign defaultGetterSetter {}
    
    "AInterface.getter"
    shared String getterSetter {
        return nonsharedGetterSetter;
    }
    "AInterface.setter"
    assign getterSetter {}
    
    "AInterface.nonsharedGetter"
    String nonsharedGetterSetter {
        return "";
    }
    "AInterface.nonsharedSetter"
    assign nonsharedGetterSetter {}
    
    "AInterface.formalMethod"
    shared formal void formalMethod(
        "AInterface.formalMethod.parameter"
        String parameter);
    
    "AInterface.defaultMethod"
    shared default void defaultMethod(
        "AInterface.defaultMethod.parameter"
        String parameter){}
    
    "AInterface.method"
    shared void method(
        "AInterface.method.parameter"
        String parameter){}
    
    "AInterface.nonsharedMethod"
    void nonsharedMethod(
        "AInterface.nonsharedMethod.parameter"
        String parameter){}
}

"AAbstractClass"
seq("AAbstractClass 1")
seq("AAbstractClass 2")
shared abstract class AAbstractClass (
    "AAbstractClass.parameter"
    String parameter) satisfies AInterface {

    "AAbstractClass.FormalInnerClass"
    shared actual class FormalInnerClass (
            "AAbstractClass.FormalInnerClass.parameter"
            String parameter)
            extends super.FormalInnerClass(parameter) {
        
    }
    
    "AAbstractClass.DefaultInnerClass"
    shared actual class DefaultInnerClass (
            "AAbstractClass.DefaultInnerClass.parameter"
            String parameter)
            extends super.DefaultInnerClass(parameter)  {
    }
    
    "AAbstractClass.formalAttributeGetter"
    shared actual String formalAttribute {
        return "";
    }
    
    "AAbstractClass.formalAttributeSetter"
    assign formalAttribute {
    }
    
    "AAbstractClass.formalMethod"
    shared actual default void formalMethod(
        "AAbstractClass.formalMethod.parameter"
        String parameter) {
        
    }
    
    "AAbstractClass.objectMember"
    shared object objectMember {}
    
    "AAbstractClass.InnerClass"
    shared class InnerClass(
            "AAbstractClass.InnerClass.parameter"
            String parameter) {
        
        "AAbstractClass.InnerClass.method"
        shared void method(
            "AAbstractClass.InnerClass.method.parameter"
            String parameter) {}
    }
    
    "AAbstractClass.InnerInterface"
    shared interface InnerInterface {
        
        "AAbstractClass.InnerInterface.method"
        shared void method(
            "AAbstractClass.InnerInterface.method.parameter"
            String parameter) {}
    }
}

"AClass"
seq("AClass 1")
seq("AClass 2")
shared class AClass(
        "AClass.parameter"
        seq("AClass.parameter 1")
        seq("AClass.parameter 2")
        String parameter) 
    extends AAbstractClass(parameter) {
 
}

decl(`value aToplevelAttribute`)
decl(`value aToplevelGetterSetter`)
decl(`function aToplevelFunction`)
decl(`value aToplevelObject`)
decl(`interface AInterface`)
decl(`function AInterface.FormalInnerClass.method`)
decl(`class AInterface.DefaultInnerClass`)
decl(`function AInterface.DefaultInnerClass.method`)
decl(`interface AInterface.SharedInnerInterface`)
decl(`function AInterface.SharedInnerInterface.method`)
decl(`value AInterface.formalAttribute`)
decl(`value AInterface.defaultGetterSetter`)
decl(`value AInterface.getterSetter`)
//decl(`value AInterface.nonsharedGetterSetter`)
decl(`function AInterface.formalMethod`)
decl(`function AInterface.defaultMethod`)
decl(`function AInterface.method`)
//decl(`function AInterface.nonsharedMethod`)
decl(`class AAbstractClass`)
decl(`class AAbstractClass.FormalInnerClass`)
decl(`class AAbstractClass.DefaultInnerClass`)
decl(`value AAbstractClass.formalAttribute`)
decl(`function AAbstractClass.formalMethod`)
decl(`value AAbstractClass.objectMember`)
decl(`class AAbstractClass.InnerClass`)
decl(`function AAbstractClass.InnerClass.method`)
decl(`interface AAbstractClass.InnerInterface`)
decl(`function AAbstractClass.InnerInterface.method`)
decl(`class AClass`)
decl(`class MetamodelRefs`)
decl(`value MetamodelRefs.parameter`)
class MetamodelRefs(
    shared String parameter) {
}
