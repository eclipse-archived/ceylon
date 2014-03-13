shared String expressionRecoveryFunctionBody(SequenceBuilder<Integer> sb) {
    sb.append(1);
    return asdfFunctionBody();
}
shared String expressionRecoveryFunctionBodyMpl(SequenceBuilder<Integer> sb1)(SequenceBuilder<Integer> sb2) {
    sb1.append(1);
    sb2.append(2);
    return asdfFunctionBodyMpl();
}
void expressionRecoveryFunctionBodyMpl_main(SequenceBuilder<Integer> sb) {
    expressionRecoveryFunctionBodyMpl(sb)(sb);
}
