import ceylon.language.model{Annotated, SequencedAnnotation}
import ceylon.language.model.declaration{Declaration}

shared final annotation class Seq(shared String seq) 
    satisfies SequencedAnnotation<Seq, Annotated> {}
shared annotation Seq seq(String s) => Seq(s); 

shared final annotation class Decl(shared Declaration decl)
    satisfies SequencedAnnotation<Decl, Annotated> {}
shared annotation Decl decl(Declaration d) => Decl(d);

"aToplevelAttribute"
seq("aToplevelAttribute 1")
seq("aToplevelAttribute 2")
see(`aToplevelGetterSetter`)
shared String aToplevelAttribute = "";

"aToplevelGetter"
seq("aToplevelGetter 1")
see(`aToplevelAttribute`)
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

decl(`aToplevelAttribute`)
decl(`aToplevelGetterSetter`)
decl(`aToplevelFunction`)
decl(`aToplevelObject`)
decl(`AInterface`)
decl(`AInterface.FormalInnerClass.method`)
decl(`AInterface.DefaultInnerClass`)
decl(`AInterface.DefaultInnerClass.method`)
decl(`AInterface.SharedInnerInterface`)
decl(`AInterface.SharedInnerInterface.method`)
decl(`AInterface.formalAttribute`)
decl(`AInterface.defaultGetterSetter`)
decl(`AInterface.getterSetter`)
//decl(`AInterface.nonsharedGetterSetter`)
decl(`AInterface.formalMethod`)
decl(`AInterface.defaultMethod`)
decl(`AInterface.method`)
//decl(`AInterface.nonsharedMethod`)
decl(`AAbstractClass`)
decl(`AAbstractClass.FormalInnerClass`)
decl(`AAbstractClass.DefaultInnerClass`)
decl(`AAbstractClass.formalAttribute`)
decl(`AAbstractClass.formalMethod`)
decl(`AAbstractClass.objectMember`)
decl(`AAbstractClass.InnerClass`)
decl(`AAbstractClass.InnerClass.method`)
decl(`AAbstractClass.InnerInterface`)
decl(`AAbstractClass.InnerInterface.method`)
decl(`AClass`)
decl(`MetamodelRefs`)
decl(`MetamodelRefs.parameter`)
class MetamodelRefs(
    shared String parameter) {
}
