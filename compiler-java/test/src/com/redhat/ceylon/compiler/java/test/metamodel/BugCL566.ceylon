import ceylon.language.meta.declaration {
    ValueDeclaration
}
class BugCl566(a) {
    shared Anything a;
}
shared void bugCl566() {
    for (option in `class BugCl566`.declaredMemberDeclarations<ValueDeclaration>()) {
        print(option);
    }
    assert(`class BugCl566`.declaredMemberDeclarations<ValueDeclaration>().size == 1);
}