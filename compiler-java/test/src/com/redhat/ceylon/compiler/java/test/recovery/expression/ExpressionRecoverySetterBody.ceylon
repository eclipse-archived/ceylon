shared String expressionRecoverySetterBody {
    return "";
}
assign expressionRecoverySetterBody {
    asdfSetterBody();
}
void expressionRecoverySetterBody_main(SequenceBuilder<Integer> sb) {
    sb.append(1);
    value x = expressionRecoverySetterBody;
    sb.append(2);
    expressionRecoverySetterBody = "";
    sb.append(3);
}
