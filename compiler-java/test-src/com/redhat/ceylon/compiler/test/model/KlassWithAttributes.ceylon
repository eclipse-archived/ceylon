class KlassWithAttributes() {
    Natural n1 = 1;
    shared Natural n2 = 2;
    variable Natural n3 := 3;
    shared variable Natural n4 := 4;
    Natural n5 {
        return 5;
    }
    shared Natural n6 {
        return 6;
    }
    Natural n7 {
        return 7;
    }
    assign n7 {
    }
    shared Natural n8 {
        return 8;
    }
    assign n8 {
    }
    
    void capture() {
        value x = n1;
        value y = n3;
//        value z = n5;
    }
}
