@nomodel
class InnerMethod() {
    void m() {
        Natural innerGetter {
            return 0;
        }
        Natural n = innerGetter;
    }
}