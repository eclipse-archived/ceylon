import ceylon.language.model { modules }

void bug1197() {
    assert(`List<String>.first`.string == "List<String>.first");
    assert(`List<String>.get`.string == "List<String>.get");
    assert(`List<String>.withLeading<Integer>`.string == "List<String>.withLeading<Integer>");
}
