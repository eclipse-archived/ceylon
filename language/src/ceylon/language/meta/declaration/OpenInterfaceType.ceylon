"An open interface type."
shared sealed interface OpenInterfaceType
    satisfies OpenClassOrInterfaceType {
    
    "This declaration's package container."
    shared actual formal InterfaceDeclaration declaration;
}
