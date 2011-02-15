doc "Attempt to evaluate the first block. If an
     exception occurs that matches the second block,
     evaluate the block."
shared Y attempt<Y,E>(Y seek(),
                      Y except(E e)) {
    try {
        return seek();
    }
    catch (E e) {
        return except(e);
    }
}