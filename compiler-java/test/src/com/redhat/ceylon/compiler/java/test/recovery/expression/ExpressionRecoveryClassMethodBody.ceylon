class ExpressionRecoveryClassMethodBody() {
    shared void method(SequenceBuilder<Integer> sb) {
        sb.append(3);
        asdfClassMethodBody();
        sb.append(4);
    }
}
void expressionRecoveryClassMethodBody_main(SequenceBuilder<Integer> sb) {
    sb.append(1);
    value i = ExpressionRecoveryClassMethodBody();
    sb.append(2);
    i.method(sb);
    sb.append(5);
}