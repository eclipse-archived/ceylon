void breakInFinally() {
    String s;
    while (true) {
        try {
            if (1==2) {
                s = "hello";
                break; // if the type checker were smarter, we could comment out this line
            } else {
                return;
            }
        } finally {
            break; // skips over the "return" in "else" above
        }
    }
    $error:"not definitely specified"
    print(s);
}

void loopInFinally() {
    String s;
    while (true) {
        try {
            if (1==2) {
                s = "hello";
                break;
            } else {
                return;
            }
        } finally {
            while (true) {
                break;
            }
        }
    }
    print(s);
}

void breakInCatch() {
    String s;
    while (true) {
        try {
            throw;
        } catch (e) {
            break;
        }
    }
    $error:"not definitely specified" 
    print(s);
}

void loopInCatch() {
    String s;
    while (true) {
        try {
            throw;
        } catch (e) {
            while (true) {
                break;
            }
        }
    }
    $error:"unreachable code" 
    print(s);
}