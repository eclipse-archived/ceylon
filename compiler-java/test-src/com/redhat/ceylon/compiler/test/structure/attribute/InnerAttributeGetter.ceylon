@nomodel
class InnerAttributeGetter() {
    void m() {
        Natural innerGetter {
            return 0;
        }
        Natural n = innerGetter;
    }
}