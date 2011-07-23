@nomodel
class InnerAttributeGetterSetter() {
    void m() {
        Natural innerGetterSetter {
            return 0;
        } assign innerGetterSetter {
        }
        Natural n = innerGetterSetter;
        innerGetterSetter := 1;
    }
}