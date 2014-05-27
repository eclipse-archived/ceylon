interface ExpressionRecoveryInterfaceMethodBody {
    void nonsharedMethod(void append(Integer i)) {
        append(3);
        asdfInterfaceMethodBody();
        append(4);
    }
    shared void other(void append(Integer i)) {
        nonsharedMethod(append);
    }
    shared void method(void append(Integer i)) {
        append(3);
        asdfInterfaceMethodBody();
        append(4);
    }
}
class ExpressionRecoveryInterfaceMethodBody_class() satisfies ExpressionRecoveryInterfaceMethodBody { }
void expressionRecoveryInterfaceMethodBody_main(void append(Integer i)) {
    append(1);
    value i = ExpressionRecoveryInterfaceMethodBody_class();
    append(2);
    i.method(append);
    append(5);
}
void expressionRecoveryInterfaceMethodBody_main2(void append(Integer i)) {
    append(1);
    value i = ExpressionRecoveryInterfaceMethodBody_class();
    append(2);
    i.other(append);
    append(5);
}