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
    satisfies ClassOrInterface<Type> 
    given Arguments satisfies Anything[] {
    
    shared formal actual ClassDeclaration declaration;
    
    "A function model for this class's initializer or default constructor,
     or null if this class has constructors but lacks a default constructor."
    shared formal <FunctionModel<Type, Arguments>|ClassModel<Type, Arguments>>? defaultConstructor;
    
    "The constructor with the given name, or null if this class lacks 
     a constructor of the given name"
    shared formal FunctionModel<Type, Arguments>|ValueModel<Type>? getConstructor<Arguments>(String name)
        given Arguments satisfies Anything[];
    
    // TODO getDeclaredConstructor (include non-shared)
    // getConstructors
    // getDeclaredConstructors
    
    // getCallableConstructor
    // getDeclaredCallableConstructor
    // getCallableConstructors
    // getDeclaredCallableConstructors
    
    // getValueConstructor
    // getDeclaredValueConstructor
    // getValueConstructors
    // getDeclaredValueConstructors
    
    // TODO plus refine those on Class to return CallableConstructor, not FunctionModel
    // and ValueConstructor not ValueModel
    // TODO plus refind them on MemberClass to return MemberClassCallableconstructor
    // and MemberClassValueConstructor not value model
    
    // TODO add getDefaultConstructor() ???
}
