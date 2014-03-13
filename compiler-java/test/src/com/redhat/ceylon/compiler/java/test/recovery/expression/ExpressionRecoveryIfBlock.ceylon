shared void expressionRecoveryIfBlock(SequenceBuilder<Integer> sb) {
    sb.append(1);
    if (1+1 == 2) {
        sb.append(2);
        asdfIfBlock();
        sb.append(3);
    }
    sb.append(4);
}