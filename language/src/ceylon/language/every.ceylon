shared Boolean every(Boolean... values) {
    for (val in values) {
        if (!val) {
            return false;
        }
    }
    return true;
}
