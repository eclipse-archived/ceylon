interface DefiniteReturn {
    
    void doSomething() {}
    void doSomethingElse() {}
    void doNothing() {}
    @error Boolean testSomething()  { @error return false; }
    class X() {}
    
    //void methods:
    
    void voidMethodWithNoReturn() {
        doSomething();
        doSomethingElse();
    }
    
    void voidMethodWithReturn() {
        doSomething();
        return;
    }
    
    void voidMethodWithThrow() {
        doSomething();
        throw;
    }
    
    void voidMethodWithReturnInIf() {
        if (testSomething()) {
            doSomething();
            return;
        }
        doSomethingElse();
    }
    
    void voidMethodWithThrowInIf() {
        if (testSomething()) {
            doSomething();
            throw;
        }
        doSomethingElse();
    }
    
    void voidMethodWithReturnInIf2() {
        if (testSomething()) {
            doSomething();
            return;
        }
        else {
            doSomethingElse();
        }
        doNothing();
    }
    
    void voidMethodWithThrowInIf2() {
        if (testSomething()) {
            doSomething();
            throw;
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
                return;
            }
        }
        else {
            doSomethingElse();
            return;
        }
    }
    
    void voidMethodWithReturnInNestedIf2() {
        if (testSomething()) {
            if (testSomething()) {
                doSomething();
                return;
            }
        }
        else {
            doSomethingElse();
        }
        return;
    }
    
    void voidMethodWithReturnInNestedIf3() {
        if (testSomething()) {
            if (testSomething()) {
                doSomething();
                return;
            }
            else {
                doNothing();
                return;
            }
        }
        else {
            doSomethingElse();
            return;
        }
    }
    
    void voidMethodWithReturnInElse() {
        if (testSomething()) {
            doSomething();
        }
        else {
            doNothing();
            return;
        }
        doSomethingElse();
    }
    
    void voidMethodWithThrowInElse() {
        if (testSomething()) {
            doSomething();
        }
        else {
            doNothing();
            throw;
        }
        doSomethingElse();
    }
    
    void voidMethodWithReturnInIfAndElse() {
        if (testSomething()) {
            doSomething();
            return;
        }
        else {
            doSomethingElse();
            return;
        }
    }
    
    void voidMethodWithThrowInIfAndElse() {
        if (testSomething()) {
            doSomething();
            throw;
        }
        else {
            doSomethingElse();
            throw;
        }
    }
    
    void voidMethodWithStatementAfterDefiniteReturn() {
        if (testSomething()) {
            doSomething();
            return;
        }
        else {
            doSomethingElse();
            return;
        }
        @error doNothing();
    }
    
    void voidMethodWithStatementAfterDefiniteThrow() {
        if (testSomething()) {
            doSomething();
            throw;
        }
        else {
            doSomethingElse();
            throw;
        }
        @error doNothing();
    }
    
    void voidMethodWithReturnInFor() {
        for (X x in {X()}) {
            doSomething();
            return;
        }
        doNothing();
    }
    
    void voidMethodWithReturnInFor2() {
        for (X x in {X()}) {
            doSomething();
            return;
        }
        fail {
            doSomethingElse();
        }
        doNothing();
    }
    
    void voidMethodWithReturnInForAndFail() {
        for (X x in {X()}) {
            doSomething();
            return;
        }
        fail {
            doSomethingElse();
            return;
        }
    }
    
    void voidMethodWithReturnInWhile() {
        while (testSomething()) {
            doSomething();
            return;
        }
        doSomethingElse();
    }
    
    void voidMethodWithReturnInWhile2() {
        while (testSomething()) {
            doSomething();
            return;
        }
        doSomethingElse();
        return;
    }
    
    void voidMethodWithReturnInDo() {
        do {
            doSomething();
            return;
        }
        while (testSomething());
    }
    
    void voidMethodWithStatementAfterReturnInDo() {
        do {
            doSomething();
            return;
        }
        while (testSomething());
        @error doSomethingElse();
    }
    
    //non-void methods
    
    @error X methodWithNoReturn() {
        doSomething();
        doSomethingElse();
    }
    
    X methodWithReturn() {
        doSomething();
        return X();
    }
    
    X methodWithThrow() {
        doSomething();
        throw;
    }
    
    @error X methodWithReturnInIf() {
        if (testSomething()) {
            doSomething();
            return X();
        }
        doSomethingElse();
    }
    
    @error X methodWithThrowInIf() {
        if (testSomething()) {
            doSomething();
            throw;
        }
        doSomethingElse();
    }
    
    @error X methodWithReturnInIf2() {
        if (testSomething()) {
            doSomething();
            return X();
        }
        else {
            doSomethingElse();
        }
        doNothing();
    }
    
    @error X methodWithThrowInIf2() {
        if (testSomething()) {
            doSomething();
            throw;
        }
        else {
            doSomethingElse();
        }
        doNothing();
    }
    
    X methodWithReturnInIf3() {
        if (testSomething()) {
            doSomething();
            return X();
        }
        else {
            doSomethingElse();
        }
        doNothing();
        return X();
    }
    
    X methodWithThrowInIf3() {
        if (testSomething()) {
            doSomething();
            throw;
        }
        else {
            doSomethingElse();
        }
        doNothing();
        throw;
    }
    
    @error X methodWithReturnInNestedIf() {
        if (testSomething()) {
            if (testSomething()) {
                doSomething();
                return X();
            }
        }
        else {
            doSomethingElse();
            return X();
        }
    }
    
    X methodWithReturnInNestedIf2() {
        if (testSomething()) {
            if (testSomething()) {
                doSomething();
                return X();
            }
        }
        else {
            doSomethingElse();
        }
        return X();
    }
    
    X methodWithReturnInNestedIf3() {
        if (testSomething()) {
            if (testSomething()) {
                doSomething();
                return X();
            }
            else {
                doNothing();
                return X();
            }
        }
        else {
            doSomethingElse();
            return X();
        }
    }
    
    @error X methodWithReturnInElse() {
        if (testSomething()) {
            doSomething();
        }
        else {
            doNothing();
            return X();
        }
        doSomethingElse();
    }
    
    @error X methodWithThrowInElse() {
        if (testSomething()) {
            doSomething();
        }
        else {
            doNothing();
            throw;
        }
        doSomethingElse();
    }
    
    X methodWithReturnInIfAndElse() {
        if (testSomething()) {
            doSomething();
            return X();
        }
        else {
            doSomethingElse();
            return X();
        }
    }
    
    X methodWithThrowInIfAndElse() {
        if (testSomething()) {
            doSomething();
            throw;
        }
        else {
            doSomethingElse();
            throw;
        }
    }
    
    X methodWithStatementAfterDefiniteReturn() {
        if (testSomething()) {
            doSomething();
            return X();
        }
        else {
            doSomethingElse();
            return X();
        }
        @error doNothing();
    }
    
    X methodWithStatementAfterDefiniteThrow() {
        if (testSomething()) {
            doSomething();
            throw;
        }
        else {
            doSomethingElse();
            throw;
        }
        @error doNothing();
    }
    
    @error X methodWithReturnInFor() {
        for (X x in {X()}) {
            doSomething();
            return X();
        }
        doNothing();
    }
    
    @error X methodWithReturnInFor2() {
        for (X x in {X()}) {
            doSomething();
            return X();
        }
        fail {
            doSomethingElse();
        }
        doNothing();
    }
    
    X methodWithReturnInFor3() {
        for (X x in {X()}) {
            doSomething();
            return X();
        }
        fail {
            doSomethingElse();
        }
        doNothing();
        return X();
    }
    
    X methodWithReturnInForAndFail() {
        for (X x in {X()}) {
            doSomething();
            return X();
        }
        fail {
            doSomethingElse();
            return X();
        }
    }
    
    @error X methodWithReturnInWhile() {
        while (testSomething()) {
            doSomething();
            return X();
        }
        doSomethingElse();
    }
    
    X methodWithReturnInWhile2() {
        while (testSomething()) {
            doSomething();
            return X();
        }
        doSomethingElse();
        return X();
    }
    
    X methodWithReturnInDo() {
        do {
            doSomething();
            return X();
        }
        while (testSomething());
    }
    
    X methodWithStatementAfterReturnInDo() {
        do {
            doSomething();
            return X();
        }
        while (testSomething());
        @error doSomethingElse();
    }
    
    //getters
    
    @error X getterWithNoReturn {
        doSomething();
        doSomethingElse();
    }
    
    X getterWithReturn {
        doSomething();
        return X();
    }
    
    X getterWithThrow {
        doSomething();
        throw;
    }
    
    @error X getterWithReturnInIf {
        if (testSomething()) {
            doSomething();
            return X();
        }
        doSomethingElse();
    }
    
    @error X getterWithThrowInIf {
        if (testSomething()) {
            doSomething();
            throw;
        }
        doSomethingElse();
    }
    
    @error X getterWithReturnInIf2 {
        if (testSomething()) {
            doSomething();
            return X();
        }
        else {
            doSomethingElse();
        }
        doNothing();
    }
    
    @error X getterWithThrowInIf2 {
        if (testSomething()) {
            doSomething();
            throw;
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
                return X();
            }
        }
        else {
            doSomethingElse();
            return X();
        }
    }
    
    X getterWithReturnInNestedIf2 {
        if (testSomething()) {
            if (testSomething()) {
                doSomething();
                return X();
            }
        }
        else {
            doSomethingElse();
        }
        return X();
    }
    
    X getterWithReturnInNestedIf3 {
        if (testSomething()) {
            if (testSomething()) {
                doSomething();
                return X();
            }
            else {
                doNothing();
                return X();
            }
        }
        else {
            doSomethingElse();
            return X();
        }
    }
    
    @error X getterWithReturnInElse {
        if (testSomething()) {
            doSomething();
        }
        else {
            doNothing();
            return X();
        }
        doSomethingElse();
    }
    
    @error X getterWithThrowInElse {
        if (testSomething()) {
            doSomething();
        }
        else {
            doNothing();
            throw;
        }
        doSomethingElse();
    }
    
    X getterWithReturnInIfAndElse {
        if (testSomething()) {
            doSomething();
            return X();
        }
        else {
            doSomethingElse();
            return X();
        }
    }
    
    X getterWithThrowInIfAndElse {
        if (testSomething()) {
            doSomething();
            throw;
        }
        else {
            doSomethingElse();
            throw;
        }
    }
    
    X getterWithStatementAfterDefiniteReturn {
        if (testSomething()) {
            doSomething();
            return X();
        }
        else {
            doSomethingElse();
            return X();
        }
        @error doNothing();
    }
    
    X getterWithStatementAfterDefiniteThrow {
        if (testSomething()) {
            doSomething();
            throw;
        }
        else {
            doSomethingElse();
            throw;
        }
        @error doNothing();
    }
    
    @error X getterWithReturnInFor {
        for (X x in {X()}) {
            doSomething();
            return X();
        }
        doNothing();
    }
    
    @error X getterWithReturnInFor2 {
        for (X x in {X()}) {
            doSomething();
            return X();
        }
        fail {
            doSomethingElse();
        }
        doNothing();
    }
    
    X getterWithReturnInFor3 {
        for (X x in {X()}) {
            doSomething();
            return X();
        }
        fail {
            doSomethingElse();
        }
        doNothing();
        return X();
    }
    
    X getterWithReturnInForAndFail {
        for (X x in {X()}) {
            doSomething();
            return X();
        }
        fail {
            doSomethingElse();
            return X();
        }
    }
    
    @error X getterWithReturnInWhile {
        while (testSomething()) {
            doSomething();
            return X();
        }
        doSomethingElse();
    }
    
    X getterWithReturnInWhile2 {
        while (testSomething()) {
            doSomething();
            return X();
        }
        doSomethingElse();
        return X();
    }
    
    X getterWithReturnInDo {
        do {
            doSomething();
            return X();
        }
        while (testSomething());
    }
    
    X getterWithStatementAfterReturnInDo {
        do {
            doSomething();
            return X();
        }
        while (testSomething());
        @error doSomethingElse();
    }
    
    //setters
    
    assign setterWithNoReturn {
        doSomething();
        doSomethingElse();
    }
    
    assign setterWithReturn {
        doSomething();
        return;
    }
    
    assign setterWithThrow {
        doSomething();
        throw;
    }
    
    assign setterWithReturnInIf {
        if (testSomething()) {
            doSomething();
            return;
        }
        doSomethingElse();
    }
    
    assign setterWithThrowInIf {
        if (testSomething()) {
            doSomething();
            throw;
        }
        doSomethingElse();
    }
    
    assign setterWithReturnInIf2 {
        if (testSomething()) {
            doSomething();
            return;
        }
        else {
            doSomethingElse();
        }
        doNothing();
    }
    
    assign setterWithThrowInIf2 {
        if (testSomething()) {
            doSomething();
            throw;
        }
        else {
            doSomethingElse();
        }
        doNothing();
    }
    
    assign setterWithReturnInNestedIf {
        if (testSomething()) {
            if (testSomething()) {
                doSomething();
                return;
            }
        }
        else {
            doSomethingElse();
            return;
        }
    }
    
    assign setterWithReturnInNestedIf2 {
        if (testSomething()) {
            if (testSomething()) {
                doSomething();
                return;
            }
        }
        else {
            doSomethingElse();
        }
        return;
    }
    
    assign setterWithReturnInNestedIf3 {
        if (testSomething()) {
            if (testSomething()) {
                doSomething();
                return;
            }
            else {
                doNothing();
                return;
            }
        }
        else {
            doSomethingElse();
            return;
        }
    }
    
    assign setterWithReturnInElse {
        if (testSomething()) {
            doSomething();
        }
        else {
            doNothing();
            return;
        }
        doSomethingElse();
    }
    
    assign setterWithThrowInElse {
        if (testSomething()) {
            doSomething();
        }
        else {
            doNothing();
            throw;
        }
        doSomethingElse();
    }
    
    assign setterWithReturnInIfAndElse {
        if (testSomething()) {
            doSomething();
            return;
        }
        else {
            doSomethingElse();
            return;
        }
    }
    
    assign setterWithThrowInIfAndElse {
        if (testSomething()) {
            doSomething();
            throw;
        }
        else {
            doSomethingElse();
            throw;
        }
    }
    
    assign setterWithStatementAfterDefiniteReturn {
        if (testSomething()) {
            doSomething();
            return;
        }
        else {
            doSomethingElse();
            return;
        }
        @error doNothing();
    }
    
    assign setterWithStatementAfterDefiniteThrow {
        if (testSomething()) {
            doSomething();
            throw;
        }
        else {
            doSomethingElse();
            throw;
        }
        @error doNothing();
    }
    
    assign setterWithReturnInFor {
        for (X x in {X()}) {
            doSomething();
            return;
        }
        doNothing();
    }
    
    assign setterWithReturnInFor2 {
        for (X x in {X()}) {
            doSomething();
            return;
        }
        fail {
            doSomethingElse();
        }
        doNothing();
    }
    
    assign setterWithReturnInForAndFail {
        for (X x in {X()}) {
            doSomething();
            return;
        }
        fail {
            doSomethingElse();
            return;
        }
    }
    
    assign setterWithReturnInWhile {
        while (testSomething()) {
            doSomething();
            return;
        }
        doSomethingElse();
    }
    
    assign setterWithReturnInWhile2 {
        while (testSomething()) {
            doSomething();
            return;
        }
        doSomethingElse();
        return;
    }
    
    assign setterWithReturnInDo {
        do {
            doSomething();
            return;
        }
        while (testSomething());
    }
    
    assign setterWithStatementAfterReturnInDo {
        do {
            doSomething();
            return;
        }
        while (testSomething());
        @error doSomethingElse();
    }
    
    //misc combinations
    
    class ClassWithReturn() {
        if (testSomething()) {
            @error return;
        }
        else {
            @error return;
        }
    }
    
    class ClassWithThrow() {
        if (testSomething()) {
            throw;
        }
        else {
            throw;
        }
    }
    
    X methodWithNestedMethod() {
        local nestedMethod() {
            return X();
        }
        return nestedMethod();
    }
    
    X getterWithNestedGetter {
        local nestedGetter {
            return X();
        }
        return nestedGetter;
    }
    
    X methodWithNestedMethodWithThrow() {
        X nestedMethod() {
            throw;
        }
        return nestedMethod();
    }
    
    X getterWithNestedGetterWithThrow {
        X nestedGetter {
            throw;
        }
        return nestedGetter;
    }
    
    X methodWithNestedClass() {
        class Nested() {
            if (testSomething()) {
                @error return X();
            }
        }
        return X();
    }
    
    X getterWithNestedClass {
        class Nested() {
            if (testSomething()) {
                @error return X();
            }
        }
        return X();
    }
    
    X methodWithNestedClassWithThrow() {
        class Nested() {
            if (testSomething()) {
                throw;
            }
        }
        return X();
    }
    
    X getterWithNestedClassWithThrow {
        class Nested() {
            if (testSomething()) {
                throw;
            }
        }
        return X();
    }
    
}