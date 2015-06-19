shared class ConstructorLiterals<T> {
    shared new (T t) {
        
    }
    shared new other(T t) {
        
    }
    shared class Member {
        shared new (T t) {
            
        }
        shared new other(T t) {
            
        }
    }
}
shared void constructorLiterals() {
    value defaultCtorDecl = `new ConstructorLiterals`;
    print(defaultCtorDecl);
    value namedCtorDecl = `new ConstructorLiterals.other`;
    print(namedCtorDecl);
    value defaultMemberCtorDecl = `new ConstructorLiterals.Member`;
    print(defaultMemberCtorDecl);
    value namedMemberCtorDecl = `new ConstructorLiterals.Member.other`;
    print(namedMemberCtorDecl);
    
    value defaultCtorModel = `ConstructorLiterals<String>`;
    print(defaultCtorModel);
    value namedCtorModel = `ConstructorLiterals<String>.other`;
    print(namedCtorModel);
    value defaultMemberCtorModel = `ConstructorLiterals<String>.Member`;
    print(defaultMemberCtorModel);
    value namedMemberCtorModel = `ConstructorLiterals<String>.Member.other`;
    print(namedMemberCtorModel);
}