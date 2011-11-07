@nomodel
shared class SequenceOperators() {
    variable Boolean b1 := false;
    variable Boolean b2 := false;
    variable Natural n1 := 0;
    variable Natural n2 := 0;
    variable Integer i1 := +0;
    variable Integer i2 := +0;
    variable Float f1 := 0.0;
    variable Float f2 := 0.0;
    
    void sequence(Correspondence<Natural, String> c1, Correspondence<Natural,String>? c2) {
        variable String? s := c1[n1];
// M2:
//        if (c1 satisfies OpenCorrespondence<Natural, String>) {
//            c1[n1] := s;
//        }
        s :=  c2?[n1];
/*
        Natural[] indices = {1, 2, 3};
        variable String[] seq1 := c1[indices];
        variable Iterable<String> it1 := c1[indices.iterator];
        variable String[] subrange = c1[n1..n2];
        variable String[] upperRange = c1[n1...];
        Natural[] spreadMember = n1[].size;
        variable Iterable<String>[] spreadInvoke = n1[].lines();
        spreadInvoke = n1[].lines{};
*/       
    }
}