class InnerMethod() {
    void m() {
        Natural inner() {
            return 0;
        }
        Natural n = inner();
    }
}