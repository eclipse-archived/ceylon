import ceylon.language.model { ... }
import ceylon.language.model.declaration { Declaration, ClassDeclaration }

final annotation class SeeThese(shared Declaration* declarations) satisfies Annotation<SeeThese> {}
annotation SeeThese seethese(Declaration* declarations) => SeeThese(*declarations);

final annotation class Meta(shared actual String string) satisfies SequencedAnnotation<Meta,Annotated> {}
annotation Meta table(String name, String schema) { return Meta(name); }
annotation Meta persistent(String column, ClassDeclaration type, Boolean update) { return Meta(column); }

"A class"
by ("Gavin King",
    "Emmanuel Bernard")
class ToplevelClass() {}

final annotation class TypeDescription(String desc) 
    satisfies OptionalAnnotation<TypeDescription,Annotated> {}

final annotation class SequencedDescription(String desc) 
    satisfies SequencedAnnotation<SequencedDescription,Annotated> {}

class Annotations() {
    
    void print("the thing to print" String text) {}
    
    deprecated void noop() {}
    
    "A class"
    by ("Gavin King",
        "Emmanuel Bernard")
    class LocalClass() {}
    
    seethese (`ToplevelClass`, `Annotations`)
    void accept(LocalClass c) {}
    
    class TrimmedString() {}
    
    table { name = "people"; 
            schema = "my"; }
    class Person() {
        
        persistent { column = "fullName";
                     update = true;
                     type = `TrimmedString`; }
        shared String name = "Gavin King";
        
    }
    
    @error print ("hello") class Broken() {}
    
    /*void getMeOutOfTheInitialiserSection(){

        Class<Annotations,[]> at = `Annotations`;
        Member<Annotations,Class<TypeDescription,[String]>> tdt = `TypeDescription`;
        Member<Annotations,Class<SequencedDescription,[String]>> sdt = `SequencedDescription`;

        TypeDescription? d = annotations<TypeDescription,TypeDescription?,Annotated>(tdt(this), at.declaration);
        SequencedDescription[] ds = annotations<SequencedDescription,SequencedDescription[],Annotated>(sdt(this), at.declaration);
    }*/
}