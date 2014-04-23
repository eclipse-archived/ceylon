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
    satisfies ClassModel<Type, Arguments> & Callable<Type, Arguments> & Applicable<Type>
    given Arguments satisfies Anything[] {
}
