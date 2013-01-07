void prob1(String|Integer x) {
    if (is String x, x.uppercased=="S") {}
    if (is String s=x, s.uppercased=="S") {}
    
    if (is Character[] x, nonempty x, x.first=="S") {}
    if (is Character[] s=x, nonempty s, s.first=="S") {}
}

Boolean prob2(Anything[] x) {
    if (exists z=x[0], is Integer y=z, y > 0) {
        return y == 1;
    }
    else {
        return false;
    }
}