class ExpressionRecoveryClassMethodSpecifier() {
    shared void method(SequenceBuilder<Integer> sb) => asdfClassMethodSpecifier();
     
}
void expressionRecoveryClassMethodSpecifier_main(SequenceBuilder<Integer> sb) {
    sb.append(1);
    value i = ExpressionRecoveryClassMethodSpecifier();
    sb.append(2);
    i.method(sb);
    sb.append(5);
}