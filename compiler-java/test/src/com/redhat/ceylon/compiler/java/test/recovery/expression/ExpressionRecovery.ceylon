// TODO Setters


shared class ExpressionRecoveryClassMembers() {
    // Errors in simple values
    shared String sharedAttributeInit = asdf();
    
    // Errors in getters
    shared String sharedAttributeBody {
        return asdf();
    }
    shared String sharedAttributeSpecifier => asdf();
    shared String sharedAttributeDeferredSpecifier;
    sharedAttributeDeferredSpecifier => asdf();
    
    // Errors in methods
    void methodBody() {
        asdf();
    }
    shared void sharedMethodBody() {
        asdf();
    }
    shared default void defaultMethodBody() {
        asdf();
    }
    String  methodSpecifier() => asdf();
    shared String  sharedMethodSpecifier() => asdf();
    shared default String  defaultMethodSpecifier() => asdf();
    
    // Errors in defaulted parameter expressions
    void methodDp(String s = asdf()) {
    }
    shared void sharedMethodDp(String s = asdf()) {
    }
    shared default void defaultMethodDp(String s = asdf()) {
    }
    shared formal void formalMethodDp(String s = asdf());
    // TODO Member classes and interfaces
}

shared interface ExpressionRecoveryInterfaceMembers {
    // Errors in simple values
    shared String sharedAttributeInit = asdf();
    
    // Errors in getters
    shared String sharedAttributeBody {
        return asdf();
    }
    shared String sharedAttributeSpecifier => asdf();
    shared String sharedAttributeDeferredSpecifier;
    sharedAttributeDeferredSpecifier => asdf();
    
    // Errors in methods
    void methodBody() {
        asdf();
    }
    shared void sharedMethodBody() {
        asdf();
    }
    shared default void defaultMethodBody() {
        asdf();
    }
    String  methodSpecifier() => asdf();
    shared String  sharedMethodSpecifier() => asdf();
    shared default String  defaultMethodSpecifier() => asdf();
    
    // Errors in defaulted parameter expressions
    void methodDp(String s = asdf()) {
    }
    shared void sharedMethodDp(String s = asdf()) {
    }
    shared default void defaultMethodDp(String s = asdf()) {
    }
    shared formal void formalMethodDp(String s = asdf());
    
    // TODO Member classes and interfaces
}

// TODO Locals
// TODO Errors in expressions in control structurs
// TODO Errors in annotations are considered errors in declarations

shared void expressionRecoveryWhile() {
    while (asdf()) {
        print("");
    }
}
shared void expressionRecoveryThrow() {
    throw asdf();
}
shared void expressionRecoveryTry() {
    try (Asdf()) {
        
    } finally {
        
    }
}

shared void expressionRecoverySwitch() {
    switch (asdf())
    case (0) {}
    else {}
}
shared void expressionRecoveryLocalValue0() {
    Integer i = 1000000000000000000000000000000000000000000000;
}
shared void expressionRecoveryLocalValue1() {
    String s = "foo``asdf``";
}
shared void expressionRecoveryLocalValue2() {
    Integer i = 1 + asdf;
}