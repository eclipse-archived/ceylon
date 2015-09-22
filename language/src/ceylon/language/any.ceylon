"Determines if any one of the given boolean values 
 (usually a comprehension) is `true`.
 
     Boolean anyNegative = any { for (x in xs) x<0.0 };
 
 If there are no boolean values, return `false`."
see (`function every`, 
     `function Iterable.any`)
tagged("Streams")
shared Boolean any({Boolean*} values) {
    for (val in values) {
        if (val) {
            return true;
        }
    }
    return false;
}
