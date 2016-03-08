@noanno
shared void bug6070() {
    Integer x;
    for (i in 0:3) {
        for (j in 0:3) {
            if (true || false) {
                x = i*10 + j;
                break;
            }
        } else {
            continue;
        }
        break;
    } else {
        x = -1;
    }
    print(x);
}
@noanno
Integer bug1227_again(Iterable<Integer> l) {
    Boolean someBoolean = nothing;
    Integer from;
    for (index in l) {
        for (index2 in l) {
            if (someBoolean) {
                from = index + index2;
                return from;
            }
        }
    }
    else {
        from = 1;
    }
    return from;
}
