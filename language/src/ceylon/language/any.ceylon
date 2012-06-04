shared Boolean any(Boolean... values) {
    for (val in values) {
        if (val) {
            return true;
        }
    }
    return false;
}
