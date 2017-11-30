void exprRecoveryMetaBadParameterType(Foo bar) {}

void exprRecoveryMeta1(Anything(Integer) progress) {
    progress(1);
    Anything x = `exprRecoveryMetaBadParameterType`;
    progress(2);
}