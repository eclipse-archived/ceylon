import ceylon.language.meta { ... }
import ceylon.language.meta.declaration { ... }
import ceylon.language.meta.model { ... }

@noanno
void bug1327(Class<DocAnnotation,[String]> docAnnotation, ValueDeclaration decl){
    value doc2 = bug1327optionalAnnotation(docAnnotation, decl);
}

@noanno
shared Value? bug1327optionalAnnotation<Value, in ProgramElement>(
    Class<OptionalAnnotation<Value,ProgramElement>> annotationType,
    ProgramElement programElement)
            given Value satisfies OptionalAnnotation<Value,ProgramElement>
            given ProgramElement satisfies Annotated { 
    return nothing;
}

class Bug1327NoBounds<T>(shared T t){}
class Bug1327Inv<T>(shared T t)
    given T satisfies String[]{}
class Bug1327Cov<out T>(shared T t)
        given T satisfies String[]{}
class Bug1327Con<in T>(T t)
        given T satisfies String[]{}

class Bug1327Inv2() extends Bug1327Inv<[]>([]){}
class Bug1327Cov2() extends Bug1327Cov<[]>([]){}
class Bug1327Con2() extends Bug1327Con<[]>([]){}

T bug1327inv<T>(T t)
        given T satisfies String[]{
    return t;
}
T bug1327cov<out T>(T t)
        given T satisfies String[]{
    return t;
}
T bug1327con<in T>(T t)
        given T satisfies String[]{
    return t;
}


@noanno
void bug1327user(){
    Empty a0 = Bug1327NoBounds([]).t;
    Empty a1 = Bug1327Inv([]).t;
    Empty a2 = Bug1327Cov([]).t;
    Bug1327Con([]);
    Empty a3 = bug1327inv([]);
    Empty a4 = bug1327cov([]);
    Empty a5 = bug1327con([]);
    Empty a6 = Bug1327Inv2().t;
    Empty a7 = Bug1327Cov2().t;
    Bug1327Con2();
}
