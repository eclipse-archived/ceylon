shared void expressionRecoveryElseIf(SequenceBuilder<Integer> sb) {
    sb.append(1);
    if (1+1 != 2) {
        sb.append(2);
    } else if (asdfElseIf()) {
        sb.append(3);
    }
    sb.append(4);
}