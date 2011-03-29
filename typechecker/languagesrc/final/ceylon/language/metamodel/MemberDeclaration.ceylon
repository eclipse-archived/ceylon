shared interface MemberDeclaration
        satisfies Declaration {

    shared formal Boolean default;
    shared formal Boolean formal;
    shared formal Boolean actual;

    shared formal TypeDeclaration declaringType;

}