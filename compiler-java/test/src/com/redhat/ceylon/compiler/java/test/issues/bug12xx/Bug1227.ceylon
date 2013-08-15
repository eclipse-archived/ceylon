@noanno
Integer bug1227_ifBreak_returnElse(Iterable<Integer> l) {
    Boolean someBoolean = nothing;
    Integer from;
    for (index in l) {
        if (someBoolean) {
            from = index;
            function ohNo() => from + 1;
            break;
        }
    }
    else {
        return 0;
    }
    return from;
}
@noanno
Integer bug1227_ifBreak_specifiedElse(Iterable<Integer> l) {
    Boolean someBoolean = nothing;
    Integer from;
    for (index in l) {
        if (someBoolean) {
            from = index;
            value x = from + 1;
            break;
        }
    }
    else {
        from = 0;
    }
    return from;
}

@noanno
Integer bug1227_ifBreak_elseBreak_specifiedElse(Iterable<Integer> l) {
    Boolean someBoolean = nothing;
    Integer from;
    for (index in l) {
        if (someBoolean) {
            from = index;
            function ohNo() => from + 1;
            break;
        } else {
            from = index + 1;
            class C() {
                value x = from;
            }
            break;
        }
    }
    else {
        from = 0;
    }
    return from;
}

@noanno
Integer bug1227_ifReturn_specifiedElse(Iterable<Integer> l) {
    Boolean someBoolean = nothing;
    Integer from;
    for (index in l) {
        if (someBoolean) {
            from = index;
            function ohNo() => from + 1;
            return from;
        }
    }
    else {
        from = 0;
    }
    return from;
}

@noanno
Integer bug1227_ifThrow_specifiedElse(Iterable<Integer> l) {
    Boolean someBoolean = nothing;
    Integer from;
    for (index in l) {
        if (someBoolean) {
            from = index;
            throw;
        }
    }
    else {
        from = 0;
    }
    return from;
}

@noanno
Integer bug1227_for2_ifReturn_specifiedElse(Iterable<Integer> l) {
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
        from = 0;
    }
    return from;
}

@noanno
Integer bug1227_declaredInside(Iterable<Integer> l) {
    Boolean someBoolean = nothing;
    for (index in l) {
        Integer from;
        if (someBoolean) {
            from = index;
            function ohNo() => from + 1;
            break;
        }
    }
    else {
        return 0;
    }
    return 0;
}

@noanno
Integer bug1227_onlySpecifiedElse(Iterable<Integer> l) {
    Boolean someBoolean = nothing;
    Integer from;
    for (index in l) {
        throw;
    }
    else {
        from = 0;
    }
    return from;
}

@noanno
Integer bug1227_function(Iterable<Integer> l) {
    Boolean someBoolean = nothing;
    Integer from();
    for (index in l) {
        if (someBoolean) {
            from = ()=>index;
            break;
        }
    }
    else {
        return 0;
    }
    return from();
}