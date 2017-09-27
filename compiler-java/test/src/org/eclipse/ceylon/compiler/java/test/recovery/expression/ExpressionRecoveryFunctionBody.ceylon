shared String expressionRecoveryFunctionBody(void append(Integer i)) {
    append(1);
    return asdfFunctionBody();
}
shared String expressionRecoveryFunctionBodyMpl(void append1(Integer i))(void append2(Integer i)) {
    append1(1);
    append2(2);
    return asdfFunctionBodyMpl();
}
void expressionRecoveryFunctionBodyMpl_main(void append(Integer i)) {
    expressionRecoveryFunctionBodyMpl(append)(append);
}
