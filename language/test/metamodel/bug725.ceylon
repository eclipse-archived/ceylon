import ceylon.language.meta { type }
import ceylon.language.meta.declaration { ValueDeclaration, FunctionDeclaration }

@test
shared void bug725() {
    assert(`class Integer`.getDeclaredMemberDeclaration<FunctionDeclaration>("plus") exists);
    assert(`class Float`.getDeclaredMemberDeclaration<FunctionDeclaration>("plus") exists);
    assert(`Integer`.getDeclaredMethod<>("plus") exists);
    assert(`Float`.getDeclaredMethod<>("plus") exists);

    assert(`class Integer`.getDeclaredMemberDeclaration<ValueDeclaration>("hash") exists);
    assert(`class Float`.getDeclaredMemberDeclaration<ValueDeclaration>("hash") exists);
    assert(`Integer`.getDeclaredAttribute<>("hash") exists);
    assert(`Float`.getDeclaredAttribute<>("hash") exists);

    assert(!`class Integer`.getDeclaredMemberDeclaration<ValueDeclaration>("integer") exists);
    assert(`class Float`.getDeclaredMemberDeclaration<ValueDeclaration>("integer") exists);
    assert(!`Integer`.getDeclaredAttribute<>("integer") exists);
    assert(`Float`.getDeclaredAttribute<>("integer") exists);

    assert(`class Integer`.getDeclaredMemberDeclaration<ValueDeclaration>("float") exists);
    assert(!`class Float`.getDeclaredMemberDeclaration<ValueDeclaration>("float") exists);
    assert(`Integer`.getDeclaredAttribute<>("float") exists);
    assert(!`Float`.getDeclaredAttribute<>("float") exists);

    assert(!`class Integer`.getDeclaredMemberDeclaration<ValueDeclaration>("finite") exists);
    assert(`class Float`.getDeclaredMemberDeclaration<ValueDeclaration>("finite") exists);
    assert(!`Integer`.getDeclaredAttribute<>("finite") exists);
    assert(`Float`.getDeclaredAttribute<>("finite") exists);
}

