import ceylon.language.metamodel { modules }

class Bug1199() {}

void bug1199() {
    assert(`Bug1199`.parameterDeclarations.empty);
}
