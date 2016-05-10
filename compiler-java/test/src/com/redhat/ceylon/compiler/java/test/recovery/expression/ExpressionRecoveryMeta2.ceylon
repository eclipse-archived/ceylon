Foo exprRecoveryMetaBadFunctionType() => nothing;

void exprRecoveryMeta2(Anything(Integer) progress) {
    progress(1);
    Anything x = `exprRecoveryMetaBadFunctionType`;
    progress(2);
}