variable Integer assertionCount:=0;
variable Integer failureCount:=0;

shared void assert(Boolean assertion, String message="") {
    assertionCount+=1;
    if (!assertion) {
        failureCount+=1;
        print("assertion failed \"" message "\"");
    }
}
shared void assertEqual(Object actual, Object expected, String message="") {
    assertionCount+=1;
    if (actual != expected) {
        failureCount+=1;
        print("assertion failed \"" message "\": '" actual "'!='" expected "'");
    }
}

shared void results() {
    print("assertions " assertionCount 
          ", failures " failureCount "");
}

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
