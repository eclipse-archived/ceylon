shared String expressionRecoverySetterSpecifier {
    return "";
}
assign expressionRecoverySetterSpecifier => asdfSetterSpecifier(expressionRecoverySetterSpecifier);

void expressionRecoverySetterSpecifier_main(void append(Integer i)) {
    append(1);
    value x = expressionRecoverySetterSpecifier;
    append(2);
    expressionRecoverySetterSpecifier = "";
    append(3);
}
