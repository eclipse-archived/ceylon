class BreakContinue() {
    void badBreak() {
        @error break;
    }
    void badContinue() {
        @error continue;
    }
    void forBreak() {
        for (n in 0..1) {
            break;
        }
    }
    void forContinue() {
        for (n in 0..1) {
            continue;
        }
    }
    void whileBreak() {
        while (1==1) {
            break;
        }
    }
    void whileContinue() {
        while (1==0) {
            continue;
        }
    }
    void forBadBreak() {
        for (n in 0..1) {
            void bad() {
                @error break;
            }
        }
    }
    void forBadContinue() {
        for (n in 0..1) {
            class Bad() {
                @error continue;
            }
        }
    }
    void forBadContinue2() {
        for (n in 0..1) {
            object bad {
                @error continue;
            }
        }
    }
    void whileBadBreak() {
        while (1==1) {
            class Bad() {
                @error break;
            }
        }
    }
    void whileBadBreak2() {
        while (1==1) {
            object bad {
                @error break;
            }
        }
    }
    void whileBadContinue() {
        while (1==0) {
            void bad() {
                @error continue;
            }
        }
    }
    
    class ReturnFromClass() {
        return;
    }
    
    object returnFromValue {
        return;
    }
    
    void returnFromClassInMethod() {
        class Good() {
            return;
        }
        return;
    }
    
    void returnFromValueInMethod() {
        object good {
            return;
        }
        return;
    }
    
    
    Integer badReturnFromSetter() {
        String bad {
            return "hello";
        }
        assign bad {
            @error return 0;
        }
        return 1;
    }
    
    Integer badReturnFromSetter2() {
        String bad {
            return "hello";
        }
        assign bad {
            @error return "goodbye";
        }
        return 1;
    }
    
    Integer returnFromSetter() {
        String bad {
            return "hello";
        }
        assign bad {
            return;
        }
        return 1;
    }
    
    Integer p() {
        for (i in 1..10) {
            return i;
        }
        @error return 1;
    }
    Integer q() {
        for (i in 1:10) {
            return i;
        }
        return 1;
    }
    
    
    Integer foo() {
        for (i in 1..10) {
            break;
            @error return i;
        }
        return 1;
    }
    
    Integer foo1() {
        for (i in 1..10) {
            break;
            @error throw;
        }
        return 1;
    }
    
    Integer foo2() {
        for (i in 1..10) {
            break;
            @error break;
        }
        return 1;
    }
    
    Integer bar() {
        for (i in 1..10) {
            if (i<6) {
                break;
            }
            return i;
        }
        return 1;
    }
    
    Integer baz() {
        for (i in 1..10) {
            if (i<6) {
                break;
            }
            else {
                break;
            }
            @error return i;
        }
        return 1;
    }
    
    Integer baz3() {
        for (i in 1..10) {
            if (i<6) {
                break;
            }
            else {
                continue;
            }
            @error return i;
        }
        return 1;
    }
    
    Integer baz2() {
        for (i in 1..10) {
            if (i<6) {
                break;
            }
            else {
                break;
            }
            @error throw;
        }
        return 1;
    }
    
    Integer baz1() {
        for (i in 1..10) {
            if (i<6) {
                break;
            }
            else {
                break;
            }
            @error continue;
        }
        return 1;
    }
    
    Integer c() {
        for (i in 1..10) {
            continue;
        }
        return 1;
    }
    
}