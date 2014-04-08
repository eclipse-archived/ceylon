@test
shared void bug411() {
    assert(`module modules.imported`.members.size == 1);
    assert(`module modules.imported`.findPackage("modules.imported") exists);
}
