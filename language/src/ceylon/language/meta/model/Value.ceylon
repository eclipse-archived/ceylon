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
        satisfies ValueModel<Get, Set>&Gettable<Get> {

    "This value's declaration."
    shared formal actual ValueDeclaration declaration;
    
    "Changes this variable's value to the given new value. Note that in the case of
     setter attributes, this can throw if the setter throws."
    throws(`class StorageException`,
        "If this attribute is not stored at runtime, for example if it is neither shared nor captured.")
    shared formal void set(Set newValue);

    "Non type-safe equivalent to [[Value.set]], to be used when you don't know the 
     variable type at compile-time. This only works if the underlying value is 
     variable. Note that if the underlying variable is a setter, this can throw 
     exceptions thrown in the setter block."
    throws(`class IncompatibleTypeException`, 
           "If the specified new value is not of a subtype of this variable's type")
    throws(`class MutationException`, 
           "If this value is not variable")
    throws(`class StorageException`,
           "If this attribute is not stored at runtime, for example if it is neither shared nor captured.")
    shared formal void setIfAssignable(Anything newValue);
}
