interface ExpressionRecoveryInterfaceMethodBody {
    void nonsharedMethod(SequenceBuilder<Integer> sb) {
        sb.append(3);
        asdfInterfaceMethodBody();
        sb.append(4);
    }
    shared void other(SequenceBuilder<Integer> sb) {
        nonsharedMethod(sb);
    }
    shared void method(SequenceBuilder<Integer> sb) {
        sb.append(3);
        asdfInterfaceMethodBody();
        sb.append(4);
    }
}
class ExpressionRecoveryInterfaceMethodBody_class() satisfies ExpressionRecoveryInterfaceMethodBody { }
void expressionRecoveryInterfaceMethodBody_main(SequenceBuilder<Integer> sb) {
    sb.append(1);
    value i = ExpressionRecoveryInterfaceMethodBody_class();
    sb.append(2);
    i.method(sb);
    sb.append(5);
}
void expressionRecoveryInterfaceMethodBody_main2(SequenceBuilder<Integer> sb) {
    sb.append(1);
    value i = ExpressionRecoveryInterfaceMethodBody_class();
    sb.append(2);
    i.other(sb);
    sb.append(5);
}