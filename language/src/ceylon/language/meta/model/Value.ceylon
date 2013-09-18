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
shared interface Value<out Type=Anything>
        satisfies ValueModel<Type> {

    "Reads the current value for this value binding. Note that in the case of getter
     values, this can throw if the getter throws."
    shared formal Type get();
}
