shared String expressionRecoveryFunctionSpecifier(void append(Integer i)) => asdfFunctionSpecifier();
shared String expressionRecoveryFunctionSpecifierMpl(void append1(Integer i))(void append2(Integer i)) => asdfFunctionSpecifierMpl();
void expressionRecoveryFunctionSpecifierMpl_main(void append(Integer i)) {
    expressionRecoveryFunctionSpecifierMpl(append)(append);
}
