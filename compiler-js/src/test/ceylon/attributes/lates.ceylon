import check { check, fail }

class LateAndLazy() {
  shared late Integer def = system.milliseconds;
  shared late Integer? opt = system.milliseconds;
  shared late variable Integer vardef = system.milliseconds;
  shared late variable Integer? varopt = system.milliseconds;
  shared late Integer late0;
}

shared late Integer latedef = system.milliseconds;
shared late Integer? lateopt = system.milliseconds;
shared late variable Integer latevardef = system.milliseconds;
shared late variable Integer? latevaropt = system.milliseconds;

shared late Integer late0;

shared class LateStatic {
  shared static late Integer def = system.milliseconds;
  shared static late Integer? opt = system.milliseconds;
  shared static variable late Integer vardef = system.milliseconds;
  shared static variable late Integer? varopt = system.milliseconds;
  shared static late Integer late0;
  shared new(){}
}

void testLazies() {
  value now = system.milliseconds;
  value stop = now+50;
  while (system.milliseconds<stop){}
  check(now<latedef, "#3544.1");
  check(now<latevardef, "#3544.2");
  if (exists x=lateopt, exists y=latevaropt) {
    check(now<x, "#3544.3");
    check(now<y, "#3544.4");
  } else {
    fail("#3544 tops");
  }
  value i=LateAndLazy();
  check(now<i.def, "#3544.5");
  check(now<i.vardef, "#3544.6");
  if (exists x=i.opt, exists y=i.varopt) {
    check(now<x, "#3544.7");
    check(now<y, "#3544.8");
  } else {
    fail("#3544 attributes");
  }
  check(now<=LateStatic.def, "#3544.9");
  check(now<=LateStatic.vardef, "#3544.10");
  if (exists x=LateStatic.opt, exists y=LateStatic.varopt) {
    check(now<x, "#3544.11");
    check(now<y, "#3544.12");
  } else {
    fail("#3544 statics");
  }
  try {
    print(late0);
    fail("#3544 late top");
  } catch (InitializationError e) {
    check(e.message=="Attempt to read uninitialized attribute «late0»", "#35442.13 message");
  }
  late0=1;
  check(late0==1, "#3544.14");
  try {
    print(i.late0);
    fail("#3544 late attribute");
  } catch (InitializationError e) {
    check(e.message=="Attempt to read uninitialized attribute «late0»", "#35442.15 message");
  }
  i.late0=1;
  check(i.late0==1, "#3544.16");
  try {
    print(LateStatic.late0>0);
    fail("#3544 late static");
  } catch (InitializationError e) {
    check(e.message=="Attempt to read uninitialized attribute «late0»", "#35442.17 message");
  }
  LateStatic.late0=1;
  check(LateStatic.late0==1, "#3544.18");
}
