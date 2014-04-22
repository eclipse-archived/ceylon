import ceylon.language.meta.declaration { ClassDeclaration }

object bug433Object {}

@test
shared void bug433() {
    assert(`value bug433Object`.isObject);
    assert(`value bug433Object`.objectDeclaration exists);
}
