import ceylon.file {
    parsePath,
    Directory
}
shared void run() {
    value fileDir = parsePath("");
    assert(is Directory fileDir);

}
