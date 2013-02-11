shared void utilCalls(){
    value t1 = [1, "a"];
    value t2 = [1, *t1];
    value t3 = [for (i in {1}) i];
    value it1 = {1, "a"};
    value it2 = {1, *it1};
    value it3 = {for (i in {1}) i};
    value entry = 1 -> 2;
    value range = 1..2;
    value segment = 1:2;
    value ignore1 = {1}*.wholePart;
    // spread sequence
    variadicMethod(1, 2, *[1]);
}

shared void variadicMethod(Integer* ints){}