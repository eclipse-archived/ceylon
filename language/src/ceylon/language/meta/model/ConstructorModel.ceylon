"A constructor model represents the model of a Ceylon class constructor 
 that you can inspect.
 
 A constructor model can be either a [[Constructor]] (when for a toplevel [[Class]]) 
 or a member [[MemberClassConstructor]] (when for a [[MemberClass]]).
 "
shared sealed interface ConstructorModel<out Type=Anything,in Arguments=Nothing>
        satisfies Functional & Declared 
        given Arguments satisfies Anything[] {
    
    shared actual formal ClassModel<Type,Nothing> container;
}