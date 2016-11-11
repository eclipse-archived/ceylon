import ceylon.language.meta{type}
@test
shared void bug6519() {
    value t = type([1, "2", true]); 
    print(t.typeArgumentList[0]);
    assert(exists e = t.typeArgumentList[0]);
    assert(e.string == "ceylon.language::true|ceylon.language::String|ceylon.language::Integer" ||
           e.string == "ceylon.language::Integer|ceylon.language::String|ceylon.language::Boolean");
}
