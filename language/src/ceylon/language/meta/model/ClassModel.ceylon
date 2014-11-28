import ceylon.language.meta.declaration {
    ClassDeclaration
}
import ceylon.language.meta.model {
    ClosedType = Type
}

"A class model represents the model of a Ceylon class that you can inspect.
 
 A class model can be either a toplevel [[Class]] or a member [[MemberClass]].
 "
shared sealed interface ClassModel<out Type=Anything, in Arguments=Nothing>
    satisfies ClassOrInterface<Type> & Functional
    given Arguments satisfies Anything[] {
    
    shared formal actual ClassDeclaration declaration;
    
    "The constructor with the given name, or null if this class lacks 
     a constructor of the given name"
    shared formal ConstructorModel<Type, Arguments>? getConstructor<Arguments=Nothing>(String name)
            given Arguments satisfies Anything[];
    
    // TODO declare instantiator here?
}
