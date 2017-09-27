@noanno
void bug6974<Type>(Anything() arg) given Type satisfies Exception {
    try {
        arg();
    } catch (Type ignored) {
    }
}
