doc "true if any of the given values is true, otherwise false"
see every
shared Boolean any(Boolean* values) {
    for (val in values) {
        if (val) {
            return true;
        }
    }
    return false;
}
