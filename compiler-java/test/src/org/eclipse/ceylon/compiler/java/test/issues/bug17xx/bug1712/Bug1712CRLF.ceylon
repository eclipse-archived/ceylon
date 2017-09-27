import ceylon.file {
    parsePath,
    Directory
}



shared void bug1712CRLF() {
    value fileDir = parsePath("");
    // look
    // comments!
    assert(is Directory fileDir);

}