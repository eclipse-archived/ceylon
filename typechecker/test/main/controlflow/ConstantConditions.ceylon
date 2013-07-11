class ConstantConditions() {
    String ifTrue() {
        if (true) {
            return "definitely";
        }
    }
    String ifFalse() {
        if (false) {} else {
            return "definitely";
        }
    }
    String whileTrue() {
        while (true) {
            if (1==0) {
                return "definitely";
            }
        }
    }
    String whileTrueBreak() {
        while (true) {
            if (1==0) {
                break;
            }
        }
        return "definitely";
    }
    String assertFalse() {
        assert (false);
    }
}