import ceylon.language.meta.declaration{ValueConstructorDeclaration}

"""Represents the model of a constructor of a Ceylon member class that you 
   can read and inspect.
   
   A member class value constructor is a member value: it is declared on classes.
   
   This is a [[Value]]: you can invoke it with an instance of the containing class
   to bind it to that instance and obtain the instance of the member class:
   
       class Outer(){
           shared class Member {
               shared new constructor {
               }
           }
       }
       
       void test(){
           MemberClassValueConstructor<Outer,Object.Member> attribute = `Outer.Member.constructor`;
           ValueConstructor<Member> boundAttribute = attribute(Outer());
           // This will print: Hello
           print(boundAttribute.get());
       }
   """

shared sealed interface MemberClassValueConstructor<in Container=Nothing, out Type=Object, in Set=Nothing>
        satisfies ValueModel<Type, Set> & Qualified<ValueConstructor<Type, Set>, Container> {
    
    "This value's declaration."
    shared formal actual ValueConstructorDeclaration declaration;
    
    "This value's closed type."
    shared formal actual MemberClass<Container, Type> type;
    
    "The class containing this constructor; the type of instances produced 
     by this constructor."
    shared actual formal ClassModel<Type> container;
    
    "Binds this attribute to the given container instance. The instance type is checked at runtime."
    throws(`class StorageException`,
        "If this attribute is not stored at runtime, for example if it is neither shared nor captured.")
    shared actual formal ValueConstructor<Type, Set> bind(Object container);
}