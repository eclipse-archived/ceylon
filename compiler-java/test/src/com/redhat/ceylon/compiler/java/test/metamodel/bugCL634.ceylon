import ceylon.language.meta.declaration {
    NestableDeclaration
}
@noanno
class BugCL634() {
    @noanno
    shared Null good = null; // regular ASCII
    @noanno
    shared Null 𝒷𝒶𝒹 = null; // Mathematical Alphanumerical Symbols – non-BMP, but no uppercase mapping
    @noanno
    shared Null 𐐨 = null; // Deseret – non-BMP and has uppercase variant
}
@noanno
shared void bugCL634() {
    assert (`class BugCL634`.declaredMemberDeclarations<NestableDeclaration>().size == 3);
    assert (`BugCL634`.getDeclaredAttributes<BugCL634>().size == 3);
    assert (`BugCL634`.getDeclaredMethods<BugCL634>() == []);
    assert ((`BugCL634.good` of Anything) exists);
    assert ((`BugCL634.𝒷𝒶𝒹` of Anything) exists);
    assert ((`BugCL634.𐐨` of Anything) exists);
    assert (!`BugCL634`.getMethod<BugCL634>("getGood") exists);
    assert (!`BugCL634`.getMethod<BugCL634>("get𝒷𝒶𝒹") exists);
    assert (!`BugCL634`.getMethod<BugCL634>("get𐐨") exists);
    assert (!`BugCL634`.getMethod<BugCL634>("get𐐀") exists);
    assert (`BugCL634`.getAttribute<BugCL634>("good") exists);
    assert (`BugCL634`.getAttribute<BugCL634>("𝒷𝒶𝒹") exists);
    assert (`BugCL634`.getAttribute<BugCL634>("𐐨") exists);
}
