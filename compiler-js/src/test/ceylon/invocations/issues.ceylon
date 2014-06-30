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

R bar326tl<A, R>(A arg) {
  assert(is R r=arg);
  return r;
}
R(A) foo326tl<A, R>() => bar326tl<A,R>;

void testIssue326() {
  check(foo326tl<Integer,Number<Integer>>()(5)==5, "Issue 326 #1");
  value ftl = foo326tl<Integer,Number<Integer>>();
  check(ftl(5)==5, "Issue 326 #2");
  R bar326<A, R>(A arg) {
    assert(is R r=arg);
    return r;
  }
  R(A) foo326<A, R>() => bar326<A,R>;
  check(foo326<Integer,Number<Integer>>()(5)==5, "Issue 326 #3");
  value fi = foo326<Integer,Number<Integer>>();
  check(fi(5)==5, "Issue 326 #4");
}

variable Integer count376=0;

[Integer,Integer,Integer] ints376() {
  count376++;
  return [1,2,3];
}
String bug376_1(Object a, Object b, Object c) => "``a``, ``b``, ``c``";
Integer bug376_2(Object a, Object b, Object c, Integer d=1) => d+1;
Integer bug376_3(Object a, Object b, Integer c=1) => c+1;
Integer bug376_4(Integer a=2, Integer b=4, Integer c=6) => a+b+c;

void testIssue376() {
  check(bug376_1(*ints376())=="1, 2, 3", "#376 invoke simple");
  check(count376 == 1, "#376 params");
  check(bug376_2(*ints376())==2, "#376 invoke defaulted 1");
  check(bug376_3(*ints376())==4, "#376 invoke defaulted 2");
  check(bug376_4(*ints376())==6, "#376 invoke defaulted 3");
  value zeroes = [0,0,0];
  check(bug376_1(*zeroes)=="0, 0, 0", "#376 0's simple");
  check(bug376_2(*zeroes)==2, "#376 0's defaulted 1");
  check(bug376_3(*zeroes)==1, "#376 0's defaulted 2");
  check(bug376_4(*zeroes)==0, "#376 0's defaulted 3");
}

void testIssues() {
  objectIssue306.foo().call();
  ClassBug314<Object>();
  methodBug314<Object>();
  value t=[1,"2"];
  methodBug314<Object>(*t);
  testIssue326();
  testIssue376();
}
