"An open class type."
shared interface OpenClassType
    satisfies OpenClassOrInterfaceType {
    
    "This class declaration."
    shared actual formal ClassDeclaration declaration;
}
