"An open class type."
shared sealed interface OpenClassType
    satisfies OpenClassOrInterfaceType {
    
    "This class declaration."
    shared actual formal ClassDeclaration declaration;
}
