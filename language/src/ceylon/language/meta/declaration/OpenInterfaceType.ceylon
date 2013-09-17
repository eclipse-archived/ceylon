"An open interface type."
shared interface OpenInterfaceType
    satisfies OpenClassOrInterfaceType {
    
    "This declaration's package container."
    shared actual formal InterfaceDeclaration declaration;
}
