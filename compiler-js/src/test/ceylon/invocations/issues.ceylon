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

class ClassBug314<T>(one=1, two="2") {
  shared Integer one;
  shared String two;
  check(one==1,"Issue 314 class");
}
void methodBug314<T>(Integer one=1, String two="2") {
  check(two=="2", "Issue 314 method");
}

void testIssues() {
  objectIssue306.foo().call();
  ClassBug314<Object>();
  methodBug314<Object>();
  value t=[1,"2"];
  methodBug314<Object>(*t);
}
