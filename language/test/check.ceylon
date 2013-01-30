variable Integer assertionCount=0;
variable Integer failureCount=0;

shared void check(Boolean assertion, String message="") {
    assertionCount+=1;
    if (!assertion) {
        failureCount+=1;
        print("**** ASSERTION FAILED \"" message "\" ****");
    }
}

shared void fail(String message) {
    check(false, message);
}

shared void results() {
    print("assertions " assertionCount 
          ", failures " failureCount "");
}
