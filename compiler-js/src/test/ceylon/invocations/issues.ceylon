import check { check }

class Issue306(String() callback) {
    shared void call() {
        check("other"==callback(), "Issue 306");
    }    
}
object objectIssue306 {
    String callback() {
        return other();
    }
    String other() {
        return "other";
    }
    shared Issue306 foo() {
        return Issue306(callback);
    }
}

void testIssues() {
  objectIssue306.foo().call();
}
