import ceylon.language.meta.declaration {
    ClassDeclaration
}
import ceylon.language.meta.model {
    ClosedType = Type
}

"A class model represents the model of a Ceylon class that you can inspect.
 
 A class model can be either a toplevel [[Class]] or a member [[MemberClass]].
 "
shared interface ClassModel<out Type=Anything, in Arguments=Nothing>
    satisfies ClassOrInterface<Type>
    given Arguments satisfies Anything[] {
    
    shared formal actual ClassDeclaration declaration;
    
    "This function's parameter closed types"
    shared formal ClosedType<Anything>[] parameterTypes;
}
