import ceylon.file {
    parsePath,
    Directory
}
shared void bug1712Mixed() {
    value fileDir = parsePath("");
    assert(is Directory fileDir);

}
