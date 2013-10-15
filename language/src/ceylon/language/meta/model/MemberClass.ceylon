"""A class model represents the model of a Ceylon class that you can instantiate and inspect.
   
   A member class is is declared on classes or interfaces.
   
   This is both a [[ClassModel]] and a [[Member]]: you can invoke it with an instance value
   to bind it to that instance and obtain a [[Class]]:

       shared class Outer(String name){
           shared class Inner(){
               shared String hello => "Hello "+name;
           }
       }
       
       void test(){
           MemberClass<Outer,Outer.Inner,[]> memberClass = `Outer.Inner`;
           Class<Outer.Inner,[]> c = memberClass(Outer("Stef"));
           // This will print: Hello Stef
           print(c().hello);
       }
 """
shared interface MemberClass<in Container, out Type=Anything, in Arguments=Nothing>
        satisfies ClassModel<Type, Arguments> & Member<Container, Class<Type, Arguments>>
        given Arguments satisfies Anything[] {
    
    shared actual formal Class<Type, Arguments> bind(Object container);
}
