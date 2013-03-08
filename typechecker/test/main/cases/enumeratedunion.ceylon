void switchUnion(String|Integer|Float val) {
    switch (val)
    case (is String) {
        val.join { "foo", "bar" };
    }
    case (is Integer) {
        val.plus(1);
    }
    case (is Float) {
        val.plus(1.0);
    }
}