native("jvm")
class JVMClass() {
}

void testBug5979(Object o) {
    $error if (o is JVMClass) {
    }
    $error if (is JVMClass o) {
    }
}
