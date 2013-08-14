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
            return "definitely";
        }
    }
    String whileTrueIf() {
        while (true) {
            if (1==0) {
                return "definitely";
            }
        }
    }
    String whileTrueIfTrue() {
        while (true) {
            if (true) {
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
    String methodWhileTrue2(Boolean b) {
        while (true) {
            if (b) {
                return "";
            }
        }
    }
    String methodWhileTrue3(Boolean b) {
        while (true) {
            if (true) {
                return "";
            }
            else {
                break;
            }
        }
    }
    String methodWhileTrue4(Boolean b) {
        while (true) {
            if (false) {
                break;
            }
            else {
                return "";
            }
        }
    }
    @error String methodWhileTrue5(Boolean b) {
        while (true) {
            if (b) {
                return "";
            }
            else {
                break;
            }
        }
    }
    String methodWhileTrue6(Boolean b) {
        while (true) {
            if (b) {
                return "";
            }
            else {
                if (false) {
                    break;
                }
            }
        }
    }
}