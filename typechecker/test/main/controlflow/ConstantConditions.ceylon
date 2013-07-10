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
            if (false) {
                return "definitely";
            }
        }
    }
    String assertFalse() {
        assert (false);
    }
}