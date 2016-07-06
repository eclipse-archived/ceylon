void exprRecoveryMetaDeclBadParameterType(Foo bar) {}

void exprRecoveryMetaDecl1(Anything(Integer) progress) {
    progress(1);
    Anything x = `function exprRecoveryMetaDeclBadParameterType`;
    progress(2);
}