@nomodel
class InnerAttributeGetter() {
    void m() {
        value m = 2;
        Natural innerGetter {
            return m;
        }
        Natural n = innerGetter;
    }
}