@nomodel
void run() {
    void holaMundo(String name="mundo") {
        print("hola " + name);
    }
    if (exists arg = process.arguments.first) {
        holaMundo(arg);
    }
    else {
        holaMundo();
    }
}