interface ExpressionRecoveryInterfaceMethodDp {
    shared void method(SequenceBuilder<Integer> sb, String s = asdfInterfaceMethodDp()) {
        sb.append(5);
    }
}
class ExpressionRecoveryInterfaceMethodDp_class() satisfies ExpressionRecoveryInterfaceMethodDp {
}
void expressionRecoveryInterfaceMethodDp_throw(SequenceBuilder<Integer> sb) {
    sb.append(1);
    value i = ExpressionRecoveryInterfaceMethodDp_class();
    sb.append(2);
    i.method(sb);
    sb.append(10);
}
void expressionRecoveryInterfaceMethodDp_nothrow(SequenceBuilder<Integer> sb) {
    sb.append(1);
    value i = ExpressionRecoveryInterfaceMethodDp_class();
    sb.append(2);
    i.method(sb, "");
    sb.append(10);
}
void expressionRecoveryInterfaceMethodDp_throw2(SequenceBuilder<Integer> sb) {
    sb.append(1);
    value i = ExpressionRecoveryInterfaceMethodDp_class();
    sb.append(2);
    value ref = i.method;
    sb.append(3);
    ref(sb);
    sb.append(10);
}
void expressionRecoveryInterfaceMethodDp_nothrow2(SequenceBuilder<Integer> sb) {
    sb.append(1);
    value i = ExpressionRecoveryInterfaceMethodDp_class();
    sb.append(2);
    value ref = i.method;
    sb.append(3);
    ref(sb, "");
    sb.append(10);
}