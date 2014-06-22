"A count of the number of `true` items in the given values.
 
     Integer negatives = count { for (x in xs) x<0.0 };"
shared Integer count({Boolean*} values) {
    variable value count=0;
    for (val in values) {
        if (val) {
            count++;
        }
    }
    return count;
}
