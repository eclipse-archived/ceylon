@noanno
class IntegerSpans(Integer first, Integer last) {
    Boolean a = nothing;
    void simple() {
        for (i in first..last) {
            print("body ``i``");
        }
    }
    void withBreak() {
        for (i in first..last) {
            if (a) {
                break;
            }
            print("body ``i``");
        }
    }
    void withContinue() {
        for (i in first..last) {
            if (a) {
                continue;
            }
            print("body ``i``");
        }
    }
    void withElseButNoExit() {
        for (i in first..last) {
            print("body ``i``");
        } else {
            print("else");
        }
    }
    void withElseAndExit() {
        for (i in first..last) {
            if (a) {
                break;
            }
            print("body ``i``");
        } else {
            print("else");
        }
    }
}
@noanno
class IntegerSpansWithStep(Integer first, Integer last, Integer step) {
    Boolean a = nothing;
    void simple() {
        for (i in (first..last).by(step)) {
            print("body ``i``");
        }
    }
    void withBreak() {
        for (i in (first..last).by(step)) {
            if (a) {
                break;
            }
            print("body ``i``");
        }
    }
    void withContinue() {
        for (i in (first..last).by(step)) {
            if (a) {
                continue;
            }
            print("body ``i``");
        }
    }
    void withElseButNoExit() {
        for (i in (first..last).by(step)) {
            print("body ``i``");
        } else {
            print("else");
        }
    }
    void withElseAndExit() {
        for (i in (first..last).by(step)) {
            if (a) {
                break;
            }
            print("body ``i``");
        } else {
            print("else");
        }
    }
}


@noanno
class CharacterSpans(Character first, Character last) {
    Boolean a = nothing;
    void simple() {
        for (i in first..last) {
            print("body ``i``");
        }
    }
    void withBreak() {
        for (i in first..last) {
            if (a) {
                break;
            }
            print("body ``i``");
        }
    }
    void withContinue() {
        for (i in first..last) {
            if (a) {
                continue;
            }
            print("body ``i``");
        }
    }
    void withElseButNoExit() {
        for (i in first..last) {
            print("body ``i``");
        } else {
            print("else");
        }
    }
    void withElseAndExit() {
        for (i in first..last) {
            if (a) {
                break;
            }
            print("body ``i``");
        } else {
            print("else");
        }
    }
}
@noanno
class CharacterSpansWithStep(Character first, Character last, Integer step) {
    Boolean a = nothing;
    void simple() {
        for (i in (first..last).by(step)) {
            print("body ``i``");
        }
    }
    void withBreak() {
        for (i in (first..last).by(step)) {
            if (a) {
                break;
            }
            print("body ``i``");
        }
    }
    void withContinue() {
        for (i in (first..last).by(step)) {
            if (a) {
                continue;
            }
            print("body ``i``");
        }
    }
    void withElseButNoExit() {
        for (i in (first..last).by(step)) {
            print("body ``i``");
        } else {
            print("else");
        }
    }
    void withElseAndExit() {
        for (i in (first..last).by(step)) {
            if (a) {
                break;
            }
            print("body ``i``");
        } else {
            print("else");
        }
    }
}