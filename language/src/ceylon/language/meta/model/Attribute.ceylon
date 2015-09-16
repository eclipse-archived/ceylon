import ceylon.language.meta.declaration{
    ValueDeclaration
}

"""An attribute model represents the model of a Ceylon attribute that you can read and inspect.
   
   An attribute is a member value: it is declared on classes or interfaces.
   
   This is both a [[ValueModel]] and a [[Member]]: you can invoke it with an instance value
   to bind it to that instance and obtain a [[Value]]:
   
       class Outer(){
           shared String foo = "Hello";
       }
       
       void test(){
           Attribute<Outer,String> attribute = `Outer.foo`;
           Value<String> boundAttribute = attribute(Outer());
           // This will print: Hello
           print(boundAttribute.get());
       }
 """
shared sealed interface Attribute<in Container=Nothing, out Get=Anything, in Set=Nothing>
        satisfies ValueModel<Get,Set> & Member<Container, Value<Get,Set>> {
    
    shared actual formal ValueDeclaration declaration;
    
    "Binds this attribute to the given container instance. The instance type is checked at runtime."
    throws(`class StorageException`,
        "If this attribute is not stored at runtime, for example if it is neither shared nor captured.")
    shared actual formal Value<Get,Set> bind(Object container);
}
