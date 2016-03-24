import java.lang {
    StringBuilder
}

@noanno
void bug1806() {
    value x = StringBuilder().length();
}
