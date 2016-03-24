"""The declaration model of a class that has constructors. For example:
   
       class Point {
           shared new(Float x, Float y) {
               // ...
           }
           shared new polar(Float r, Float theta) {
               // ...
           }
           shared new origin {
               // ...
           }
       }
       
   Such classes may not have a default (unnamed) constructor,
   so [[defaultConstructor|ClassDeclaration.defaultConstructor]] 
   has optional type.
   """
see(`interface ClassWithInitializerDeclaration`)
shared sealed interface ClassWithConstructorsDeclaration 
        satisfies ClassDeclaration {
}