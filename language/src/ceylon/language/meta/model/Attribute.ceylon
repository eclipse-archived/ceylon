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
shared interface Attribute<in Container, out Type=Anything>
        satisfies ValueModel<Type> & Member<Container, Value<Type>> {
}
