shared void expressionRecoveryElseIf(void append(Integer i)) {
    append(1);
    if (1+1 != 2) {
        append(2);
    } else if (asdfElseIf()) {
        append(3);
    }
    append(4);
}