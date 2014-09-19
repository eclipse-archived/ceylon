import ceylon.language.meta { type, modules }

@test
shared void bug536() {
    assert(type(true).string == "ceylon.language::true");
    assert(type(false).string == "ceylon.language::false");
    assert(type(null).string == "ceylon.language::null");
    assert(type(larger).string == "ceylon.language::larger");
    assert(type(1).string == "ceylon.language::Integer");
    assert(type(1.0).string == "ceylon.language::Float");
    assert(type(runtime).string == "ceylon.language::runtime");
    assert(type(language).string == "ceylon.language::language");
    assert(type(operatingSystem).string == "ceylon.language::operatingSystem");
    assert(type(process).string == "ceylon.language::process");
    assert(type(modules).string == "ceylon.language.meta::modules");
}
