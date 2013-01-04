import check {...}

String firstName = "Gavin";

String lastName {
    return "King";
}

variable Integer flag = 0;
assign lastName {
    //print(lastName);
    flag = 1;
}

shared void test() {
    checkEqual(lastName, "King", "toplevel getter");
    lastName = "Duke";
    checkEqual(flag, 1, "toplevel setter");
    
    Integer x { return 5; }
    assign x { flag = 2; }
    checkEqual(x, 5, "local getter");
    x = 7;
    checkEqual(flag, 2, "local setter");

    testNewSyntax();    
    results();
}
