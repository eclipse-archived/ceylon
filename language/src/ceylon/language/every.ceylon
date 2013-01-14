doc "true if every one of the given values is true, otherwise false."
see any
shared Boolean every(Boolean* values) {
    for (val in values) {
        if (!val) {
            return false;
        }
    }
    return true;
}
