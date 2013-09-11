import ceylon.language.model { modules }

void bugC1197() {
    assert(`List<String>.first`.string == "ceylon.language::List<ceylon.language::String>.first");
    assert(`List<String>.get`.string == "ceylon.language::List<ceylon.language::String>.get");
    assert(`List<String>.withLeading<Integer>`.string == "ceylon.language::List<ceylon.language::String>.withLeading<ceylon.language::Integer>");
}
