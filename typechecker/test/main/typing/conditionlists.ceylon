void prob1(String|Character[]|Integer x) {
    if (is String x, x.uppercased=="S") {}
    if (is String s=x, s.uppercased=="S") {}
    
    if (is Character[] x, nonempty x, x.first=="S") {}
    if (is Character[] s=x, nonempty s, s.first=="S") {}
    
    switch (x)
    case (is {Character*}) {}
    case (is Integer) {}
}

Boolean prob2(Anything[] x) {
    if (exists z=x[0], is Integer y=z, y > 0) {
        return y == 1;
    }
    else {
        return false;
    }
}

void scoping() {
    assert (exists xxx = parseInteger("1"),
            exists yyy = parseInteger("2"),
            exists zzz = parseInteger(yyy.string+xxx.string),
            exists @error zzz = parseInteger(""));
    @error value xxx = 2;
    @type:"Integer" value x = xxx;
    @type:"Integer" value y = yyy;
    @type:"Integer" value z = zzz;
}