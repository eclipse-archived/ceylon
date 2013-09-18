import ceylon.language.meta.declaration {
    InterfaceDeclaration
}

"An interface model represents the model of a Ceylon interface that you can inspect.
 
 An interface model can be either a toplevel [[Interface]] or a member [[MemberInterface]].
 "
shared interface InterfaceModel<out Type=Anything>
    satisfies ClassOrInterface<Type> {
    
    shared formal actual InterfaceDeclaration declaration;
}
