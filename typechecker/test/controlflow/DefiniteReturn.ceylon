interface DefiniteReturn {
    
    void doSomething() {}
    void doSomethingElse() {}
    void doNothing() {}
    @error Boolean testSomething()  { @error return false }
    
    void voidMethodWithNoReturn() {
        doSomething();
        doSomethingElse();
    }
    
    void voidMethodWithReturn() {
        doSomething();
        return
    }
    
    void voidMethodWithThrow() {
        doSomething();
        throw
    }
    
    void voidMethodWithReturnInIf() {
        if (testSomething()) {
            doSomething();
            return
        }
        doSomethingElse();
    }
    
    void voidMethodWithThrowInIf() {
        if (testSomething()) {
            doSomething();
            throw
        }
        doSomethingElse();
    }
    
    void voidMethodWithReturnInIf2() {
        if (testSomething()) {
            doSomething();
            return
        }
        else {
            doSomethingElse();
        }
        doNothing();
    }
    
    void voidMethodWithThrowInIf2() {
        if (testSomething()) {
            doSomething();
            throw
        }
        else {
            doSomethingElse();
        }
        doNothing();
    }
    
    void voidMethodWithReturnInNestedIf() {
        if (testSomething()) {
            if (testSomething()) {
                doSomething();
                return
            }
        }
        else {
            doSomethingElse();
            return
        }
    }
    
    void voidMethodWithReturnInNestedIf2() {
        if (testSomething()) {
            if (testSomething()) {
                doSomething();
                return
            }
        }
        else {
            doSomethingElse();
        }
        return
    }
    
    void voidMethodWithReturnInNestedIf3() {
        if (testSomething()) {
            if (testSomething()) {
                doSomething();
                return
            }
            else {
                doNothing();
                return
            }
        }
        else {
            doSomethingElse();
            return
        }
    }
    
    void voidMethodWithReturnInElse() {
        if (testSomething()) {
            doSomething();
        }
        else {
            doNothing();
            return
        }
        doSomethingElse();
    }
    
    void voidMethodWithThrowInElse() {
        if (testSomething()) {
            doSomething();
        }
        else {
            doNothing();
            throw
        }
        doSomethingElse();
    }
    
    void voidMethodWithReturnInIfAndElse() {
        if (testSomething()) {
            doSomething();
            return
        }
        else {
            doSomethingElse();
            return
        }
    }
    
    void voidMethodWithThrowInIfAndElse() {
        if (testSomething()) {
            doSomething();
            throw
        }
        else {
            doSomethingElse();
            throw
        }
    }
    
    void voidMethodWithStatementAfterDefiniteReturn() {
        if (testSomething()) {
            doSomething();
            return
        }
        else {
            doSomethingElse();
            return
        }
        @error doNothing();
    }
    
    void voidMethodWithStatementAfterDefiniteThrow() {
        if (testSomething()) {
            doSomething();
            throw
        }
        else {
            doSomethingElse();
            throw
        }
        @error doNothing();
    }
    
    class X() {}
    
    @error X methodWithNoReturn() {
        doSomething();
        doSomethingElse();
    }
    
    X methodWithReturn() {
        doSomething();
        return X()
    }
    
    X methodWithThrow() {
        doSomething();
        throw
    }
    
    @error X methodWithReturnInIf() {
        if (testSomething()) {
            doSomething();
            return X()
        }
        doSomethingElse();
    }
    
    @error X methodWithThrowInIf() {
        if (testSomething()) {
            doSomething();
            throw
        }
        doSomethingElse();
    }
    
    @error X methodWithReturnInIf2() {
        if (testSomething()) {
            doSomething();
            return X()
        }
        else {
            doSomethingElse();
        }
        doNothing();
    }
    
    @error X methodWithThrowInIf2() {
        if (testSomething()) {
            doSomething();
            throw
        }
        else {
            doSomethingElse();
        }
        doNothing();
    }
    
    @error X methodWithReturnInNestedIf() {
        if (testSomething()) {
            if (testSomething()) {
                doSomething();
                return
            }
        }
        else {
            doSomethingElse();
            return
        }
    }
    
    X methodWithReturnInNestedIf2() {
        if (testSomething()) {
            if (testSomething()) {
                doSomething();
                return
            }
        }
        else {
            doSomethingElse();
        }
        return
    }
    
    X methodWithReturnInNestedIf3() {
        if (testSomething()) {
            if (testSomething()) {
                doSomething();
                return
            }
            else {
                doNothing();
                return
            }
        }
        else {
            doSomethingElse();
            return
        }
    }
    
    @error X methodWithReturnInElse() {
        if (testSomething()) {
            doSomething();
        }
        else {
            doNothing();
            return X()
        }
        doSomethingElse();
    }
    
    @error X methodWithThrowInElse() {
        if (testSomething()) {
            doSomething();
        }
        else {
            doNothing();
            throw
        }
        doSomethingElse();
    }
    
    X methodWithReturnInIfAndElse() {
        if (testSomething()) {
            doSomething();
            return X()
        }
        else {
            doSomethingElse();
            return X()
        }
    }
    
    X methodWithThrowInIfAndElse() {
        if (testSomething()) {
            doSomething();
            throw
        }
        else {
            doSomethingElse();
            throw
        }
    }
    
    X methodWithStatementAfterDefiniteReturn() {
        if (testSomething()) {
            doSomething();
            return X()
        }
        else {
            doSomethingElse();
            return X()
        }
        @error doNothing();
    }
    
    X methodWithStatementAfterDefiniteThrow() {
        if (testSomething()) {
            doSomething();
            throw
        }
        else {
            doSomethingElse();
            throw
        }
        @error doNothing();
    }
    
    @error X getterWithNoReturn {
        doSomething();
        doSomethingElse();
    }
    
    X getterWithReturn {
        doSomething();
        return X()
    }
    
    X getterWithThrow {
        doSomething();
        throw
    }
    
    @error X getterWithReturnInIf {
        if (testSomething()) {
            doSomething();
            return X()
        }
        doSomethingElse();
    }
    
    @error X getterWithThrowInIf {
        if (testSomething()) {
            doSomething();
            throw
        }
        doSomethingElse();
    }
    
    @error X getterWithReturnInIf2 {
        if (testSomething()) {
            doSomething();
            return X()
        }
        else {
            doSomethingElse();
        }
        doNothing();
    }
    
    @error X getterWithThrowInIf2 {
        if (testSomething()) {
            doSomething();
            throw
        }
        else {
            doSomethingElse();
        }
        doNothing();
    }
    
    @error X getterWithReturnInNestedIf {
        if (testSomething()) {
            if (testSomething()) {
                doSomething();
                return
            }
        }
        else {
            doSomethingElse();
            return
        }
    }
    
    X getterWithReturnInNestedIf2 {
        if (testSomething()) {
            if (testSomething()) {
                doSomething();
                return
            }
        }
        else {
            doSomethingElse();
        }
        return
    }
    
    X getterWithReturnInNestedIf3 {
        if (testSomething()) {
            if (testSomething()) {
                doSomething();
                return
            }
            else {
                doNothing();
                return
            }
        }
        else {
            doSomethingElse();
            return
        }
    }
    
    @error X getterWithReturnInElse {
        if (testSomething()) {
            doSomething();
        }
        else {
            doNothing();
            return X()
        }
        doSomethingElse();
    }
    
    @error X getterWithThrowInElse {
        if (testSomething()) {
            doSomething();
        }
        else {
            doNothing();
            throw
        }
        doSomethingElse();
    }
    
    X getterWithReturnInIfAndElse {
        if (testSomething()) {
            doSomething();
            return X()
        }
        else {
            doSomethingElse();
            return X()
        }
    }
    
    X getterWithThrowInIfAndElse {
        if (testSomething()) {
            doSomething();
            throw
        }
        else {
            doSomethingElse();
            throw
        }
    }
    
    X getterWithStatementAfterDefiniteReturn {
        if (testSomething()) {
            doSomething();
            return X()
        }
        else {
            doSomethingElse();
            return X()
        }
        @error doNothing();
    }
    
    X getterWithStatementAfterDefiniteThrow {
        if (testSomething()) {
            doSomething();
            throw
        }
        else {
            doSomethingElse();
            throw
        }
        @error doNothing();
    }
    
    class ClassWithReturn() {
        if (testSomething()) {
            @error return
        }
        else {
            @error return
        }
    }
    
    class ClassWithThrow() {
        if (testSomething()) {
            throw
        }
        else {
            throw
        }
    }
    
    X methodWithNestedMethod() {
        local nestedMethod() {
            return X()
        }
        return nestedMethod()
    }
    
    X getterWithNestedGetter {
        local nestedGetter {
            return X()
        }
        return nestedGetter
    }
    
    X methodWithNestedMethodWithThrow() {
        X nestedMethod() {
            throw
        }
        return nestedMethod()
    }
    
    X getterWithNestedGetterWithThrow {
        X nestedGetter {
            throw
        }
        return nestedGetter
    }
    
    X methodWithNestedClass() {
        class Nested() {
            if (testSomething()) {
                @error return X()
            }
        }
        return X()
    }
    
    X getterWithNestedClass {
        class Nested() {
            if (testSomething()) {
                @error return X()
            }
        }
        return X()
    }
    
    X methodWithNestedClassWithThrow() {
        class Nested() {
            if (testSomething()) {
                throw
            }
        }
        return X()
    }
    
    X getterWithNestedClassWithThrow {
        class Nested() {
            if (testSomething()) {
                throw
            }
        }
        return X()
    }
    
}