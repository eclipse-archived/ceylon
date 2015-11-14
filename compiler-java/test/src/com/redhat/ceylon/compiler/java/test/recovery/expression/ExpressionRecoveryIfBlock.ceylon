shared void expressionRecoveryIfBlock(void append(Integer i)) {
    append(1);
    if (1+1 == 2) {
        append(2);
        asdfIfBlock();
        append(3);
    }
    append(4);
}