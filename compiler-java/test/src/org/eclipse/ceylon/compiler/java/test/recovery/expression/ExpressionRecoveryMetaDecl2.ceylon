Foo exprRecoveryMetaDeclBadFunctionType() => nothing;

void exprRecoveryMetaDecl2(Anything(Integer) progress) {
    progress(1);
    Anything x = `function exprRecoveryMetaDeclBadFunctionType`;
    progress(2);
}