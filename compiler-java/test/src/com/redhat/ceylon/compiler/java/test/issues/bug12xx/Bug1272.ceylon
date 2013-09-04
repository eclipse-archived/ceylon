import ceylon.language.model{Class, OptionalAnnotation, Annotated}
import ceylon.language.model.declaration{ValueDeclaration}
import ceylon.language.model{optionalAnnotation}

@noanno
Value? bug1272<Value,ProgramElement>(
            Class<OptionalAnnotation<Value,ProgramElement>> annotationType,
            ProgramElement programElement)
        given Value satisfies OptionalAnnotation<Value,ProgramElement>
        given ProgramElement satisfies Annotated { 
    return null;
}

@noanno
void bug1272_callsite() {
    Class<Shared, []> s = `Shared`;
    ValueDeclaration x => nothing;
    bug1272(s, x);// This is OK!
    Shared? srd = bug1272(s, x); // This causes javac stackoverflow
    //assert(bug1272(s, x) exists);
}
