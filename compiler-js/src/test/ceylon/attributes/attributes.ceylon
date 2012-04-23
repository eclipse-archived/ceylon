import assert {...}

String firstName = "Gavin";

String lastName {
    return "King";
}

variable Integer flag := 0;
assign lastName {
    //print(lastName);
    flag := 1;
}

shared void test() {
    assertEqual(lastName, "King", "toplevel getter");
    lastName := "Duke";
    assertEqual(flag, 1, "toplevel setter");
    
    Integer x { return 5; }
    assign x { flag := 2; }
    assertEqual(x, 5, "local getter");
    x := 7;
    assertEqual(flag, 2, "local setter");
    
    results();
}
