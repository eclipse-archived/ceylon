"Determines if any one of the given boolean values 
 (usually a comprehension) is `true`.
 
     Boolean anyNegative = any { for (x in xs) x<0.0 };"
see (`function every`, 
     `function Iterable.any`)
shared Boolean any({Boolean*} values) {
    for (val in values) {
        if (val) {
            return true;
        }
    }
    return false;
}
