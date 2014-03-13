class ExpressionRecoveryClassMethodSpecifierDeferred() {
    shared void method(SequenceBuilder<Integer> sb);
    method = asdfClassMethodSpecifierDeferred();
     
}
void expressionRecoveryClassMethodSpecifierDeferred_main(SequenceBuilder<Integer> sb) {
    sb.append(1);
    value i = ExpressionRecoveryClassMethodSpecifierDeferred();
    sb.append(2);
    i.method(sb);
    sb.append(5);
}