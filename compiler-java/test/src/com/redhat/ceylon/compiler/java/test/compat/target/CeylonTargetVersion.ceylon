import com.redhat.ceylon.common {
    Versions
}

shared void run() {
    print("Compiled with ``Versions.\iCEYLON_VERSION_NUMBER`` according to com.redhat.ceylon.common::Versions");
    assert("1.2.1" == Versions.\iCEYLON_VERSION_NUMBER);
    print("Running on ``language.version`` according to language.version");
    assert("1.2.0" == language.version);
}