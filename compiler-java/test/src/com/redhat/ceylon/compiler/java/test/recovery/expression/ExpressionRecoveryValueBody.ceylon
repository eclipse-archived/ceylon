shared String expressionRecoveryValueBody {
    expressionRecoveryValueBodySb.append(2);
    return asdfValueBody();
}
variable SequenceBuilder<Integer> expressionRecoveryValueBodySb = SequenceBuilder<Integer>();
void expressionRecoveryValueBody_main(SequenceBuilder<Integer> sb) {
    expressionRecoveryValueBodySb = sb;
    sb.append(1);
    value x = expressionRecoveryValueBody;
    sb.append(3);
}
