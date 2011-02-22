doc "If the condition is true, evaluate first block,
     otherwise, evaluate second block. Return result
     of evaluation."
shared Y when<Y>(Boolean condition,
                 Y then(),
                 Y otherwise()=get(null)) {
    if (condition) {
        return then();
    }
    else {
        return otherwise();
    }
}