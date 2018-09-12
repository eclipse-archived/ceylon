void selfTypeCoverage() {
    Comparable<Integer> k = 1;
    Integer kk = k of Integer;
    Comparable<out Integer> i = 1;
    Integer ii = i of Integer;
    Comparable<in Integer> j = 1;
    $error Integer jj = j of Integer;
}