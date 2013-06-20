"Determines if every one of the given boolean values 
 (usually a comprehension) is `true`."
//see (`any`)
shared Boolean every({Boolean*} values) {
    for (val in values) {
        if (!val) {
            return false;
        }
    }
    return true;
}
