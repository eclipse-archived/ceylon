@test
shared void comprehensions() {
  //Used in initializers
  value s1 = [ for (w in {"hello", "world"}) for (c in w) c.string ];
  value s2 = [ for (w in {"hello", "world"}) for (c in w) if (c in "hw") c.uppercased ];
  value s3 = [ for (c in "hello") c.string ];
  //simple invocation
  check(Array{for (c in "hello") c.string}==Array{"h", "e", "l", "l", "o"}, "comprehensions 1");
  //named arg invocation - not callable anymore with a comprehension
  /*check(array {
    for (c in "hello") c.string
  } == Array("h", "e", "l", "l", "o"), "comprehensions 2");*/
  check(s1=={ "h", "e", "l", "l", "o", "w", "o", "r", "l", "d" }.sequence(), "comprehensions 3");
  check(s2=={ 'H', 'W' }.sequence(), "comprehensions 4");
  check(s3=={ "h", "e", "l", "l", "o" }.sequence(), "comprehensions 5");
  check([for (y in 1..5) for (x in 1..5) if (x>y) x*y]=={2, 3, 4, 5, 6, 8, 10, 12, 15, 20}.sequence(), "comprehensions 6");
  check([for (x in 1..6) if (x % 2 == 0) for (y in 1..3) x*y]=={2,4,6,4,8,12,6,12,18}.sequence(), "comprehensions 7");
  check([for (x in 1..6) if (x % 2 == 0) for (y in 1..3) if ((x*y)%3==0) x*y]=={6,12,6,12,18}.sequence(), "comprehensions 8");
  check([for (x in 1..10) if (x%2==0) if (x>5) x]=={6,8,10}.sequence(), "comprehensions 9");
  variable Object varcomp = s1;
  check(varcomp is Iterable<Anything>, "comprehension is Iterable");
  check([for (x in {null, "hello", "goodbye"}) if (exists x) if (x.size>5) x]=={"goodbye"}.sequence(), "comprehensions w/exists 1");
  check([for (x in {"a", "", "c"}) if (exists c=x[0]) c.uppercased]=={'A', 'C'}.sequence(), "comprehensions w/exists 2");
  check([for (x in {"a", "", "c"}) if (!x.empty) x.uppercased]=={"A", "C"}.sequence(), "comprehensions w/nonempty 1");
  check([for (x in {"a", "", "c"}) if (!x.empty) x.uppercased]=={"A", "C"}.sequence(), "comprehensions w/nonempty 2");
  check([for (x in {1,2,"3.1",4}) if (is String x) x.reversed]=={"1.3"}.sequence(), "comprehensions w/is 1");
  check([for (x in {1.1,2.2,3,4.4}) if (is Integer i=x) i*2]=={6}.sequence(), "comprehensions w/is 2");
  check(Array{for (k->v in ["a","b","c","d","e"].indexed) if (k%2==0) v.uppercased}==Array({"A","C","E"}), "key-value comprehensions");
  // comprehension nested inside comprehension
  check([for(i in 1..2)[for(j in 1..2)"``i``,``j``"]]==[["1,1","1,2"],["2,1","2,2"]], "nested comprehension ``[for(i in 1..2)[for(j in 1..2)"``i``,``j``"]]`` instead of {{1,1,1,2},{2,1,2,2}}");
  //comprehensions beginning with if clause: ceylon-spec#869
  {String*}? strings = { "Hello", "beautiful", "World" };
  value existingStrings = strings;
  assert (exists existingStrings);
  check([if (is {String*} strings) strings] == { existingStrings }.sequence(), "comprehensions starting with if 1: if");
  check([if (is {String*} strings) for (string in strings) string] == existingStrings.sequence(), "comprehensions starting with if 2: if for");
  check([if (is {String*} strings) for (string in strings) if (exists first=string.first, first.uppercase) string] == {"Hello", "World"}.sequence(), "comprehensions starting with if 3: if for if");
  check([if (2 + 2 == 4) if ((1..10).size==10) if (is {String*} strings) for (string in strings) string.uppercased] == {"HELLO", "BEAUTIFUL", "WORLD"}.sequence(), "comprehensions starting with if 4: if if if for");
  check([if (1+1==2) if (2+2==5) if (2+3==5) true] == empty, "comprehensions starting with if 5: if ifFalse if");
  check([if (1+1==2) if (2+2==4) if (2+2==5) true] == empty, "comprehensions starting with if 6: if if ifFalse");
  check([if (2+2==5) if (1+1==2) if (2+2==4) true] == empty, "comprehensions starting with if 7: ifFalse if if");
  variable value test=0;
  function f() => test++>0;
  check(test==0, "comprehensions starting with if 8: general laziness");
  f();
  check(test==1, "comprehensions starting with if 8: variable capture");
  check([if (f()) 1] == [1], "comprehensions starting with if 8: collection");
  check(test==2, "comprehensions starting with if 8: evaluation expected 2, got ``test``");
  test=0;
  check([if (!f()) 1] nonempty, "comprehensions starting with if 8: emptiness");
  test=2;
  check(![if (!f()) 1] nonempty, "comprehensions starting with if 8: nonemptiness");
  check(test==3, "comprehensions starting with if 8: evaluation 2 expected 3, got ``test``");
  value c1 = {if (f()) if (!f()) 1};
  check(test==3, "comprehensions starting with if 8: lazy evaluation 3 expected 3, got ``test``");
  check(c1.size==0, "comprehensions starting with if 8: emptiness");
  check(test==5, "comprehensions starting with if 8: reevaluation expected 5 got ``test``");
  value c2 = {if (f()) if (!f()) if (f()) 1};
  check(test==5, "comprehensions starting with if 9: lazy evaluation expected 5 got ``test``");
  check(c2.size==0, "comprehensions starting with if 9: emptiness");
  check(test==7, "comprehensions starting with if 9: reevaluation expected 7 got ``test``");

  //new comprehension-related functions
  check(any { for (x in 1..5) x>4 }, "any");
  check(every { for (x in 1..5) x>0 }, "every");
/*  if (exists ff=first { for (x in 1..5) if (x>3) x }) {
    check(ff==4, "first [1]");
  } else { fail("first [1]"); }
  check(!first { for (x in 1..5) if (x%6==0) x } exists, "first [2]");*/
  check(count { for (x in 1..5) x>4 } == 1, "count [1]");
  check(count { for (x in 1..5) x>0 } == 5, "count [2]");

  //^^^^^^*Newly found bugs here
  //ceylon-compiler#598
  value b598 = [ for (x in 0..10) if (x%2==0) x^2 ];
  check(b598 is Sequence<Anything>, "ceylon-compiler #598 [1]");
  check(b598.string=="[0, 4, 16, 36, 64, 100]", "ceylon-compiler #598 [2]");
  //ceylon-compiler#599
  value b599_1 = [for (x in "hello") x];
  value b599_2 = [*b599_1];
  check(b599_2 is Sequence<Anything>, "ceylon-compiler #599 [1]");
  check(b599_1.sequence() == b599_2, "ceylon-compiler #599 [2]");
  //ceylon-compiler#601
  Iterable<String>? b601 = { [ for (s in "hello world".split()) s ], {""} }.first;
  if (exists b601) {
    check(b601=={"hello", "world"}.sequence(), "ceylon-compiler #601 [1]");
  } else { fail("ceylon-compiler #601 [2]"); }
  Iterable<String> b85 = {for (k->v in ["a","b","c","d","e"].indexed) if (k%2==0) v.uppercased};
  Iterator<String> iter85 = b85.iterator();
  if (is String x=iter85.next()) {
    check(x=="A", "ceylon-language #85.1");
  } else { fail("ceylon-language #85.1"); }
  if (is String x=iter85.next()) {
    check(x=="C", "ceylon-language #85.2");
  } else { fail("ceylon-language #85.2"); }
  if (is String x=iter85.next()) {
    check(x=="E", "ceylon-language #85.3");
  } else { fail("ceylon-language #85.3"); }
  if (is String x=iter85.next()) {
    fail("ceylon-language #85.4");
  }
  //ceylon-js#532
  value boom = {1, nothing};
  check({ for (elem in boom) elem }.take(1).sequence()==[1],"js#532.1 comprehension not lazy enough");
  check({ for (elem in boom) for (sub in boom) sub }.take(1).sequence()==[1],"js#532.2 comprehension not lazy enough");

  //Capture: had problems on both backends
  value capvalues = [ 100, 110, 120 ];
  value capvaluesNull = [ 100, 110, 120, null ];
  value capfuncs0 = {
      for (v in capvalues)
      ()=>v
  };
  check([ for (capfun in capfuncs0.sequence()) capfun() ]==capvalues, "Comprehension capture 1");
  value capfuncs1 = {
      for (v in capvaluesNull)
      if (exists w=v)
      ()=>w
  };
  check([ for (capfun in capfuncs1.sequence()) capfun() ]==capvalues, "Comprehension capture 2");
  value capfuncs2 = {
      for (v in capvalues)
      if (exists w = true then (()=>v))
      w
  };
  check([ for (capfun in capfuncs2.sequence()) capfun() ]==capvalues, "Comprehension capture #3");
  value capfuncs3 = {
      for (v in capvalues)
      for (w in { ()=>v })
      w
  };
  check([ for (capfun in capfuncs3.sequence()) capfun() ]==capvalues, "Comprehension capture #4");
  check([ for (k->v in "hey".indexed) for (s in {v}) s ]==['h','e','y'], "#5993");
}
