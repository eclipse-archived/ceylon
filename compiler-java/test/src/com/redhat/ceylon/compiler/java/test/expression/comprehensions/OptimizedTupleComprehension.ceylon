Integer[] optimizedTupleComprehension() {
    Integer[] xs = [0,1,2,3,4,5,6,7,8,9];
    return [ for (x in xs) x ];
}
