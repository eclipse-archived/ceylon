import ceylon.language.metamodel { ... }
import ceylon.language.metamodel.declaration { Declaration }

annotation class SeeThese(shared Declaration* declarations) satisfies Annotation<SeeThese> {}
annotation SeeThese seethese(Declaration* declarations) => SeeThese(*declarations);

Null table(String name, String schema) { return null; }
Null persistent(String column, Anything type, Boolean update) { return null; }

"A class"
by ("Gavin King",
    "Emmanuel Bernard")
class ToplevelClass() {}

annotation class TypeDescription(String desc) 
    satisfies OptionalAnnotation<TypeDescription,Annotated> {}

annotation class SequencedDescription(String desc) 
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
                     type = TrimmedString; }
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