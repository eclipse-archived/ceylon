@nomodel
shared class EqualityAndComparisonOperators() {
    variable Boolean b1 := false;
    variable Natural n1 := 0;
    variable Natural n2 := 0;

    void equalityAndComparison(IdentifiableObject o1, IdentifiableObject o2) {
        b1 := o1 === o2;
        b1 := n1 == n2;
        b1 := n1 != n2;
        Comparison c = n1 <=> n2;
        b1 := n1 < n2;
        b1 := n1 > n2;
        b1 := n1 <= n2;
        b1 := n1 >= n2;
        b1 := n1 in n1..n2;
        b1 := n1 is Natural;
        b1 := n1 extends Natural;
        b1 := n1 satisfies Object;
    }
}