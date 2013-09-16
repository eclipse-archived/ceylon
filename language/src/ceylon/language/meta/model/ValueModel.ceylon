import ceylon.language.meta.declaration {
    ValueDeclaration
}
import ceylon.language.meta.model {
    ClosedType = Type
}

shared interface ValueModel<out Type=Anything>
        satisfies Model {

    shared formal actual ValueDeclaration declaration;
    
    shared formal ClosedType<Type> type;
}
