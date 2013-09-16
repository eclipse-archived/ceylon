import ceylon.language.meta { modules }

class BugC1199() {}

void bugC1199() {
    assert(`class BugC1199`.parameterDeclarations.empty);
}
