import ceylon.language.meta { modules }

@test
shared void bugC1197() {
    assert(`List<String>.first`.string == "ceylon.language::List<ceylon.language::String>.first");
    assert(`List<String>.get`.string == "ceylon.language::List<ceylon.language::String>.get");
    assert(`Sequential<String>.withLeading<Integer>`.string == "ceylon.language::Sequential<ceylon.language::String>.withLeading<ceylon.language::Integer>");
}
