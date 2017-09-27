import java.io {
    File
}

void bug6289() {
    print(File("hello").absolutePath?.split('/'.equals)?.sequence());
}