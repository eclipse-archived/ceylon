import java.lang { NullPointerException }

@nomodel
void test() {
    try {
        throw NullPointerException();
    }
    catch (NullPointerException npe) {}
}