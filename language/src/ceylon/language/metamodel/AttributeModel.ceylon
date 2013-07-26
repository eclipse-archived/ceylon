import ceylon.language.metamodel.declaration {
    ValueDeclaration
}
import ceylon.language.metamodel {
    ClosedType = Type
}

shared interface AttributeModel<out Type>
        satisfies Model {

    shared formal actual ValueDeclaration declaration;
    
    shared formal ClosedType type;
}
