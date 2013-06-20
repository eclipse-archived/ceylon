import ceylon.language.metamodel { ... }

Null table(String name, String schema) { return null; }
Null persistent(String column, Anything type, Boolean update) { return null; }

class Annotations() {
    
    void print("the thing to print" String text) {}
    
    deprecated void noop() {}
    
    "A class"
    by ("Gavin King",
        "Emmanuel Bernard")
    class LocalClass() {}
    
    see (`LocalClass`, `Annotations`)
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
    
    annotation class TypeDescription(String desc) 
        satisfies OptionalAnnotation<TypeDescription,Annotated> {}
    
    annotation class SequencedDescription(String desc) 
        satisfies SequencedAnnotation<SequencedDescription,Annotated> {}

    //temporary errors until we got metatypes done 
    Class<Annotations,[]> at = `Annotations`;
    Class<TypeDescription,[String]> tdt = `TypeDescription`;
    Class<SequencedDescription,[String]> sdt = `SequencedDescription`;
    
    TypeDescription? d = annotations<TypeDescription,TypeDescription?,Annotated>(tdt, at.declaration);
    SequencedDescription[] ds = annotations<SequencedDescription,SequencedDescription[],Annotated>(sdt, at.declaration);
}