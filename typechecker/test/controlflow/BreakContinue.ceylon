class BreakContinue() {
    void badBreak() {
        @error break;
    }
    void badContinue() {
        @error continue;
    }
    void forBreak() {
        for (local n in 0..1) {
            break;
        }
    }
    void forContinue() {
        for (local n in 0..1) {
            continue;
        }
    }
    void whileBreak() {
        while (true) {
            break;
        }
    }
    void whileContinue() {
        while (false) {
            continue;
        }
    }
    void forBadBreak() {
        for (local n in 0..1) {
            void bad() {
                @error break;
            }
        }
    }
    void forBadContinue() {
        for (local n in 0..1) {
            class Bad() {
                @error continue;
            }
        }
    }
    void whileBadBreak() {
        while (true) {
            class Bad() {
                @error break;
            }
        }
    }
    void whileBadContinue() {
        while (false) {
            void bad() {
                @error continue;
            }
        }
    }
    
    class BadReturn() {
        @error return;
    }
    
    void badReturnFromClass() {
        class Bad() {
            @error return;
        }
    }
    
    
    Natural badReturnFromSetter() {
        String bad {
            return "hello";
        }
        assign bad {
            @error return 0;
        }
        return 1;
    }
    
    Natural returnFromSetter() {
        String bad {
            return "hello";
        }
        assign bad {
            return;
        }
        return 1;
    }
    
}