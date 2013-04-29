interface DefiniteAssignment {
    
    class X() {}
    void doSomething() {}
    void doSomethingElse() {}
    void doNull() {}
    void use(X x) {}
    Boolean testSomething()  { return 1>100; }
    
    //void methods:
    
    void goodMethodWithNoSpec() {
        variable X x;
        doSomething();
        doSomethingElse();
    }
    
    void badMethodWithNoSpec() {
        variable X x;
        doSomething();
        doSomethingElse();
        @error use(x);
    }
    
    void goodMethodWithSpec() {
        variable X x;
        doSomething();
        x = X();
        doSomethingElse();
        use(x);
    }
    
    void badMethodWithSpec() {
        variable X x;
        doSomething();
        @error use(x);
        doSomethingElse();
        x = X();
    }
    
    void badMethodWithRepeatedSpec() {
        variable X x;
        doSomething();
        x = X();
        doSomethingElse();
        x = X();
    }
        
    void badMethodWithRecursiveSpec() {
        variable X x;
        doSomething();
        @error x = x;
        doSomethingElse();
        use(x);
    }
    
    void badMethodWithRecursiveSpec2() {
        @error variable X x = x;
        doSomething();
        use(x);
    }
    
    void goodMethodWithRecursiveSpec3() {
        variable X y = X();
        variable X x = y;
        use(x);
    }
    
    void goodMethodWithSpecInIf() {
        variable X x;
        if (testSomething()) {
            doSomething();
            x = X();
            doSomethingElse();
            use(x);
        }
        doNull();
    }
    
    void badMethodWithSpecInIf() {
        variable X x;
        if (testSomething()) {
            doSomething();
            x = X();
            doSomethingElse();
        }
        doNull();
        @error use(x);
    }
    
    void goodMethodWithSpecInIf2() {
        variable X x;
        if (testSomething()) {
            x = X();
            doSomething();
            use(x);
        }
        else {
            doSomethingElse();
        }
        doNull();
    }
    
    void badMethodWithSpecInIf2() {
        variable X x;
        if (testSomething()) {
            doSomething();
            x = X();
        }
        else {
            doSomethingElse();
            @error use(x);
        }
        doNull();
    }
    
    void badMethodWithSpecInIf3() {
        variable X x;
        if (testSomething()) {
            doSomething();
            x = X();
            use(x);
        }
        else {
            doSomethingElse();
        }
        doNull();
        x = X();
    }
    
    void badMethodWithSpecInIf4() {
        variable X x = X();
        doNull();
        if (testSomething()) {
            doSomething();
            x = X();
        }
    }
    
    void badMethodWithSpecInIf5() {
        variable X x;
        doNull();
        x = X();
        if (testSomething()) {
            doSomething();
            x = X();
        }
    }
    
    void goodMethodWithSpecInNestedIf() {
        variable X x;
        if (testSomething()) {
            x = X();
            if (testSomething()) {
                doSomething();
                use(x);
            }
            else {
                use(x);
            }
        }
        else {
            doSomethingElse();
        }
    }
    
    void badMethodWithSpecInNestedIf() {
        variable X x;
        if (testSomething()) {
            if (testSomething()) {
                x = X();
                doSomething();
                use(x);
            }
            else {
                @error use(x);
            }
        }
        else {
            doSomethingElse();
        }
    }
        
    void goodMethodWithSpecInElse() {
        variable X x;
        if (testSomething()) {
            doSomething();
        }
        else {
            x = X();
            doNull();
            use(x);
        }
        doSomethingElse();
    }
    
    void badMethodWithSpecInElse() {
        variable X x;
        if (testSomething()) {
            doSomething();
            @error use(x);
        }
        else {
            x = X();
            doNull();
            use(x);
        }
        doSomethingElse();
    }
    
    void badMethodWithSpecInElse2() {
        variable X x;
        if (testSomething()) {
            doSomething();
        }
        else {
            x = X();
            doNull();
            use(x);
        }
        doSomethingElse();
        x = X();
    }
    
    void badMethodWithSpecInElse3() {
        variable X x = X();
        doSomethingElse();
        if (testSomething()) {
            doSomething();
        }
        else {
            x = X();
            doNull();
        }
    }
    
    void badMethodWithSpecInElse4() {
        variable X x;
        doSomethingElse();
        x = X();
        if (testSomething()) {
            doSomething();
        }
        else {
            x = X();
            doNull();
        }
    }
    
    void goodMethodWithSpecInIfAndElse() {
        variable X x;
        if (testSomething()) {
            x = X();
            doSomething();
            use(x);
        }
        else {
            x = X();
            doSomethingElse();
            use(x);
        }
    }
    
    void goodMethodWithSpecInIfAndElse2() {
        variable X x;
        if (testSomething()) {
            doSomething();
            x = X();
        }
        else {
            doSomethingElse();
            x = X();
        }
        use(x);
    }
    
    void goodMethodWithRecursiveSpecInIfAndElse() {
        variable X y;
        if (testSomething()) {
            y = X();
        }
        else {
            y = X();
            use(y);
        }
        variable X x = y;
        use(x);
    }
    
    void badMethodWithRecursiveSpecInIf() {
        variable X y;
        if (testSomething()) {
            y = X();
        }
        @error variable X x = y;
    }
    
    void goodMethodWithSpecInFor() {
        for (X x in {X()}) {
            variable X y;
            doSomething();
            y = x;
        }
        doNull();
    }
    
    void badMethodWithSpecInFor() {
        variable X y;
        for (X x in {X()}) {
            doSomething();
            y = x;
        }
        doNull();
    }
    
    void goodMethodWithSpecInFail() {
        variable X y;
        for (X x in {X()}) {
            doSomething();
        }
        else {
            doSomethingElse();
            y = X();
        }
        doNull();
        use (y);
    }
    
    void goodMethodWithSpecInFail2() {
        variable X y;
        for (X x in {X()}) {
            doSomething();
            y = X();
        }
        else {
            doSomethingElse();
            y = X();
        }
        doNull();
        use (y);
    }
    
    void badMethodWithSpecInFail() {
        for (X x in {X()}) {
            variable X y;
            doSomething();
        }
        else {
            doSomethingElse();
            @error y = X();
        }
        doNull();
    }
    
    void badMethodWithSpecInFail3() {
        variable X y;
        for (X x in {X()}) {
            doSomething();
            break;
        }
        else {
            doSomethingElse();
            y = X();
        }
        doNull();
        @error use (y);
    }
    
    void goodMethodWithSpecInWhile() {
        while (testSomething()) {
            variable X x;
            doSomething();
            x = X();
        }
        doSomethingElse();
    }
    
    void badMethodWithSpecInWhile() {
        variable X x;
        while (testSomething()) {
            doSomething();
            x = X();
        }
        doSomethingElse();
        @error use (x);
    }
    
    void goodMethodWithSpecInWhile2() {
        variable X x;
        while (testSomething()) {
            doSomething();
            x = X();
        }
        x = X();
        doSomethingElse();
        use (x);
    }
    
    /*void goodMethodWithSpecInDo() {
        do {
            variable X x;
            doSomething();
            x = X();
        }
        while (testSomething());
    }
    
    void badMethodWithSpecInDo() {
        variable X x;
        do {
            doSomething();
            x = X();
        }
        while (testSomething());
    }*/
    
    class E() extends Exception() {}
    
    void try1() {
        variable X x;
        try {
            x = X();
        }
        finally {}
        use(x);
    }

    void tryCatch1() {
        variable X x;
        try {
            x = X();
            use(x);
        }
        catch (Exception e) {
            x = X();
            use(x);
        }
        use(x);
    }

    void tryCatch2() {
        variable X x;
        try {
        }
        catch (Exception e) {
            x = X();
            use(x);
        }
        @error use(x);
    }

    void tryCatch3() {
        variable X x;
        try {
            x = X();
            use(x);
        }
        catch (Exception e) {
        }
        @error use(x);
    }

    void tryCatchCatch1() {
        variable X x;
        try {
            x = X();
        }
        catch (E e) {
            x = X();
        }
        catch (Exception e) {
            x = X();
        }
        use(x);
    }

    void tryCatchCatch2() {
        variable X x;
        try {
            x = X();
        }
        catch (E e) {
            x = X();
        }
        catch (Exception e) {
        }
        @error use(x);
    }

    void tryFinally1() {
        variable X x;
        try {
        }
        finally {
            x = X();
        }
        use(x);
    }

    void tryFinally2() {
        variable X x;
        try {
            x = X();
        }
        finally {
            x = X();
        }
        use(x);
    }

    void tryFinally3() {
        variable X x;
        try {
            x = X();
        }
        finally {
            @error use(x);
        }
        use(x);
    }

    void tryCatchFinally1() {
        variable X x;
        try {
        }
        catch (Exception e) {
        }
        finally {
            x = X();
        }
        use(x);
    }

    void tryCatchFinally2() {
        variable X x;
        try {
        }
        catch (Exception e) {
            x = X();
        }
        finally {
            x = X();
        }
        use(x);
    }
    
    void switchCase0() {
        Boolean b = true;
        variable String s;
        switch (b)
        case (true) {
            s = "hello";
        }
        case (false) {
            s = "there";
            print(s);
        }
        //TODO: remove
        else {
            s = "world";
        }
        print(s);
    }
    
    void switchCase1() {
        Boolean b = true;
        variable String s;
        switch (b)
        case (true) {
            s = "hello";
        }
        case (false) {
            return;
        }
        //TODO: remove
        else {
            s = "world";
        }
        print(s);
    }
    
    void switchCase2() {
        Boolean b = true;
        variable String s;
        switch (b)
        case (true) {
            s = "hello";
        }
        case (false) {
        }
        //TODO: remove
        else {
            s = "world";
        }
        @error print(s);
    }
    
    void switchCase3() {
        Boolean b = true;
        variable String s;
        switch (b)
        case (true) {
            s = "hello";
        }
        case (false) {
            s = "world";
        }
        //TODO: remove
        else {
        }
        @error print(s);
    }
    
    class GoodWithAnonFunction() {
        String name = "hello";
        value x = () => name;
    }

    class BadWithAnonFunction() {
        String name;
        @error value x = () => name;
    }
    
}