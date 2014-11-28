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
shared sealed interface Class<out Type=Anything, in Arguments=Nothing>
    satisfies ClassModel<Type, Arguments> & Applicable<Type, Arguments>
    given Arguments satisfies Anything[] {
    
    "The constructor with the given name, or null if this class lacks 
     a constructor of the given name"
    shared actual formal Constructor<Type, Arguments>? getConstructor<Arguments=Nothing>(String name)
            given Arguments satisfies Anything[];
    
    "An applicable way of instantiating this class via its initializer 
     or default constructor, or null if this class lacks both an initializer 
     and default constructor."
    shared formal Functional&Applicable<Type>? instantiator<Arguments>()
            given Arguments satisfies Anything[];
}
