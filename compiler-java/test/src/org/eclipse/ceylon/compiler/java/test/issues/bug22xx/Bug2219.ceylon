@noanno
shared void bug2219() {
    Integer|Float foo = 1;
    String s = String(
        switch (foo)
        case (is Integer) "integer"
        case (is Float) "float");
}
