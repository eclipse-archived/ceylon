"Determines if every one of the given boolean values 
 (usually a comprehension) is `true`.
 
     Boolean allPositive = every { for (x in xs) x>0.0 };
 
 If there are no boolean values, return `true`."
see (`function any`, 
     `function Iterable.every`)
tagged("Streams")
shared Boolean every({Boolean*} values) {
    for (val in values) {
        if (!val) {
            return false;
        }
    }
    return true;
}
