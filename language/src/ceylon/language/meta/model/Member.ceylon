import ceylon.language.meta.model { AppliedType = Type }

"""Model for members that can be bound to a containing instance to turn them into toplevel models.
   
   You can bind a member to an instance by invoking that member with the instance as parameter:
   
       shared class Outer(String name){
           shared class Inner(){
               shared String hello => "Hello "+name;
           }
       }
       
       void test(){
           Member<Outer,Class<Outer.Inner,[]>> memberClass = `Outer.Inner`;
           Class<Outer.Inner,[]> c = memberClass(Outer("Stef"));
           // This will print: Hello Stef
           print(c().hello);
       }
   """
shared interface Member<in Container, out Kind> 
        satisfies Kind(Container)
        given Kind satisfies Model {
    
    "The declaring closed type. This is the type that declared this member."
    shared formal AppliedType<Anything> declaringType;
    
    "Type-unsafe container binding, to be used when the container type is unknown until runtime.
     
     This has the same behaviour as invoking this `Member` directly, but exchanges compile-time type
     safety with runtime checks."
    throws(`class IncompatibleTypeException`, "If the container is not assignable to this member's container")
    shared formal Model bind(Anything container);
}