"Determines if every one of the given boolean values 
 (usually a comprehension) is `true`.
 
     Boolean allPositive = every { for (x in xs) x>0.0 };"
see (`function any`, 
     `function Iterable.every`)
shared Boolean every({Boolean*} values) {
    for (val in values) {
        if (!val) {
            return false;
        }
    }
    return true;
}
