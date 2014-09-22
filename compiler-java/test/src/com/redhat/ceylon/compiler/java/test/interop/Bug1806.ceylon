import java.lang {
    StringBuilder
}

void bug1806() {
    value x = StringBuilder().length();
}
