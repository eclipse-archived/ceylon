doc "If the value is non-null, evaluate first block,
     otherwise, evaluate second block. Return result
     of evaluation."
shared Y with<X,Y>(specified X? value,
                   Y then(coordinated X x),
                   Y otherwise()=get(null)) {
    if (exists value) {
        return then(value);
    }
    else {
        return otherwise();
    }
}