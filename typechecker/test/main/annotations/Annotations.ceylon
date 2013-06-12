import ceylon.language.metamodel { ... }

Null table(String name, String schema) { return null; }
Null persistent(String column, Anything type, Boolean update) { return null; }

class Annotations() {
    
    void print("the thing to print" String text) {}
    
    deprecated void noop() {}
    
    "A class"
    by ("Gavin King",
        "Emmanuel Bernard")
    class Class() {}
    
    see (Class, Annotations)
    void accept(Class c) {}
    
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
    
    class TypeDescription(String desc) 
        satisfies OptionalAnnotation<TypeDescription,Type<Object>> {}
    
    class SequencedDescription(String desc) 
        satisfies SequencedAnnotation<SequencedDescription,Annotated> {}

    //temporary errors until we got metatypes done 
    @error Type<Annotations> at = Annotations;
    @error Type<TypeDescription> tdt = TypeDescription;
    @error Type<SequencedDescription> sdt = SequencedDescription;
    TypeDescription? d = annotations<TypeDescription,TypeDescription?,Type<Object>>(tdt, at);
    SequencedDescription[] ds = annotations<SequencedDescription,SequencedDescription[],Type<Object>>(sdt, at);
    
}