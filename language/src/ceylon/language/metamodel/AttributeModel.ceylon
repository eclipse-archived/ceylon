import ceylon.language.metamodel.declaration {
    AttributeDeclaration
}
import ceylon.language.metamodel {
    ClosedType = Type
}

shared interface AttributeModel<out Type>
        satisfies Model {

    shared formal actual AttributeDeclaration declaration;
    
    shared formal ClosedType type;
}
