import ceylon.language.meta.declaration { FunctionDeclaration }
import ceylon.language.meta { type }

shared final annotation class Bug719SomeAnnotation(shared String someName, shared String somePath)
        satisfies OptionalAnnotation<Bug719SomeAnnotation, FunctionDeclaration> {}

shared annotation Bug719SomeAnnotation bug719some (String someName, String somePath) 
        => Bug719SomeAnnotation(someName, somePath);

shared class Bug719AnnoMethod() {
    shared 
    bug719some("some", "somePath") 
    void annotated() {
        
    }
    
    shared
    void notAnnotated() {
        
    }
}

@test
shared void bug719() {
    Bug719AnnoMethod annoMethod = Bug719AnnoMethod();
    
    assert(type(annoMethod).getDeclaredMethods<Bug719AnnoMethod,Anything,Nothing>().size == 2);
    assert(type(annoMethod).getMethods<Bug719AnnoMethod,Anything,Nothing>(`Bug719SomeAnnotation`).size == 1);
    assert(`Bug719AnnoMethod`.getMethods<Bug719AnnoMethod,Anything,Nothing>(`Bug719SomeAnnotation`).size == 1);
    
    assert(`Bug719AnnoMethod`.declaration
        .annotatedMemberDeclarations<FunctionDeclaration, Bug719SomeAnnotation>().size == 1);
}

