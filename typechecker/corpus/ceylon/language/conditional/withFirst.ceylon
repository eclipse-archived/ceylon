doc "If the given list is nonempty, evaluate first block,
     otherwise, evaluate second block. Return result
     of evaluation."
shared Y withFirst<X,Y>(X[] list,
                        Y pop(X first, X... rest),
                        Y otherwise()=get(null)) {
    if (exists X first = list.first) {
        return pop(first, list.rest);
    }
    else {
        return otherwise();
    }
}