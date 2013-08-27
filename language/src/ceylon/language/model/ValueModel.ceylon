import ceylon.language.model.declaration {
    ValueDeclaration
}
import ceylon.language.model {
    ClosedType = Type
}

shared interface ValueModel<out Type>
        satisfies Model {

    shared formal actual ValueDeclaration declaration;
    
    shared formal ClosedType type;
}
