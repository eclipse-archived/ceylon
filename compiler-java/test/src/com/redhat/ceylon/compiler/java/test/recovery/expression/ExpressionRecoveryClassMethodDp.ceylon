class ExpressionRecoveryClassMethodDp() {
    shared void method(SequenceBuilder<Integer> sb, String s = asdfClassMethodDp()) {
        sb.append(5);
    }
}
void expressionRecoveryClassMethodDp_throw(SequenceBuilder<Integer> sb) {
    sb.append(1);
    value i = ExpressionRecoveryClassMethodDp();
    sb.append(2);
    i.method(sb);
    sb.append(10);
}
void expressionRecoveryClassMethodDp_nothrow(SequenceBuilder<Integer> sb) {
    sb.append(1);
    value i = ExpressionRecoveryClassMethodDp();
    sb.append(2);
    i.method(sb, "");
    sb.append(10);
}
void expressionRecoveryClassMethodDp_throw2(SequenceBuilder<Integer> sb) {
    sb.append(1);
    value i = ExpressionRecoveryClassMethodDp();
    sb.append(2);
    value ref = i.method;
    sb.append(3);
    ref(sb);
    sb.append(10);
}
void expressionRecoveryClassMethodDp_nothrow2(SequenceBuilder<Integer> sb) {
    sb.append(1);
    value i = ExpressionRecoveryClassMethodDp();
    sb.append(2);
    value ref = i.method;
    sb.append(3);
    ref(sb, "");
    sb.append(10);
}