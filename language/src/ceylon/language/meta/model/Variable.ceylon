import ceylon.language.meta.declaration { VariableDeclaration }

"""A variable model represents the model of a Ceylon variable that you can read, write and inspect.
   
   A variable is a mutable toplevel binding, declared on a package.
   
   This is a [[Value]] that you can use to change a value declaration's current value:
   
       shared variable String foo = "Hello";
       
       void test(){
           Variable<String> val = `foo`;
           // This will print: Hello
           print(val.get());
           val.set("Bonjour");
           // This will print: Bonjour
           print(val.get());
       }
 """
shared interface Variable<Type> 
        satisfies Value<Type> {
    
    "The variable declaration for this variable."
    shared formal actual VariableDeclaration declaration;

    "Changes this variable's value to the given new value. Note that in the case of
     setter attributes, this can throw if the setter throws."
    shared formal void set(Type newValue);
}
