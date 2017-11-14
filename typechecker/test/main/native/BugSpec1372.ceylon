shared native class BugSpec1372() {
    shared native BugSpec1372 returnMe();
}

shared native("jvm") class BugSpec1372() {
    shared native("jvm") BugSpec1372 returnMe() {
        throw Exception("BugSpec1372-JVM");
    }
}

native("jvm")
void testBugSpec1372() {
    BugSpec1372().returnMe();
}
