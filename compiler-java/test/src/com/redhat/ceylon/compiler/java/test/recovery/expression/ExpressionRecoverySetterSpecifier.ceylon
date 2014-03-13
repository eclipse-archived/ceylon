shared String expressionRecoverySetterSpecifier {
    return "";
}
assign expressionRecoverySetterSpecifier => asdfSetterSpecifier(expressionRecoverySetterSpecifier);

void expressionRecoverySetterSpecifier_main(SequenceBuilder<Integer> sb) {
    sb.append(1);
    value x = expressionRecoverySetterSpecifier;
    sb.append(2);
    expressionRecoverySetterSpecifier = "";
    sb.append(3);
}
