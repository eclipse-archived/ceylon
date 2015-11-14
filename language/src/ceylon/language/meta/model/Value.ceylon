import ceylon.language.meta.declaration { ValueDeclaration }

"""A value model represents the model of a Ceylon value that you can read and inspect.
   
   A value is a toplevel binding, declared on a package.
   
   This is a [[ValueModel]] that you can query for a value declaration's current value:
   
       shared String foo = "Hello";
       
       void test(){
           Value<String> val = `foo`;
           // This will print: Hello
           print(val.get());
       }
 """
shared sealed interface Value<out Get=Anything, in Set=Nothing>
        satisfies ValueModel<Get, Set>&Gettable<Get, Set> {

    "This value's declaration."
    shared formal actual ValueDeclaration declaration;
    
    
}
