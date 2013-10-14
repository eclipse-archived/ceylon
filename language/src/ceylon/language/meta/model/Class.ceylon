"""A class model represents the model of a Ceylon class that you can instantiate and inspect.
   
   A class is a toplevel type, declared on a package.
   
   This is a [[ClassModel]] that you can also invoke to instantiate new instances of the class:
   
       shared class Foo(String name){
           shared String hello => "Hello "+name;
       }
       
       void test(){
           Class<Foo,[String]> c = `Foo`;
           // This will print: Hello Stef
           print(c("Stef").hello);
       }
 """
shared interface Class<out Type=Anything, in Arguments=Nothing>
    satisfies ClassModel<Type, Arguments> & Callable<Type, Arguments>
    given Arguments satisfies Anything[] {
    
    "Type-unsafe function application, to be used when the argument types are unknown until runtime.
     
     This has the same behaviour as invoking this Function directly, but exchanges compile-time type
     safety with runtime checks."
    throws(`class IncompatibleTypeException`, "If any argument is not assignable to this function's corresponding parameter")
    throws(`class InvocationException`, "If there are not enough or too many provided arguments")
    shared formal Type apply(Anything* arguments);
}
