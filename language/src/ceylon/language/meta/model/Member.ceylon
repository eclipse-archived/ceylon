import ceylon.language.meta.model { AppliedType = Type }

shared interface Member<in Type, out Kind> 
        satisfies Kind(Type)
        given Kind satisfies Model {
    
    shared formal AppliedType<Anything> declaringClassOrInterface;
}