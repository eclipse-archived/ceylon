import ceylon.language.meta.declaration{ValueConstructorDeclaration}

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
shared sealed interface ValueConstructor<out Type=Object,in Set=Nothing>
        satisfies ValueModel<Type, Set> & Gettable<Type, Set> {
    
    "This value's declaration."
    shared formal actual ValueConstructorDeclaration declaration;
    
    "This value's closed type."
    shared formal actual Class<Type> type;
    
    "The class containing this constructor; the type of instances produced 
     by this constructor."
    shared actual formal Class<Type>? container;
}

