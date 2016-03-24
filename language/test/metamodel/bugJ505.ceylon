import ceylon.language.meta { type }
import ceylon.language.meta.model { ClassModel, Class, MemberClass }

object jsTop505{
  shared class Inner(){}
  shared ClassModel test1() {
    class Test1(){}
    return `Test1`;
  }
  shared ClassModel test2() {
    class Test2(){}
    return type(Test2());
  }
}

@test
shared void bugJ505() {
  object o505{}
  assert(!`class o505`.objectValue exists);
  class Test505(){}
  Object t1=`Test505`;
  assert(t1 is Class);
  assert(type(Test505()) is Class);
  assert(type(jsTop505.Inner()) is MemberClass<Nothing>);
  assert(jsTop505.test1() is Class);
  assert(jsTop505.test2() is Class);
}
