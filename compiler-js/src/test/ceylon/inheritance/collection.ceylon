import assert {...}

class TestColl(Integer... elements) satisfies Collection<Integer> {

  shared actual TestColl clone { return TestColl(elements...); }
  shared actual Integer size = elements.size;
  shared actual Iterator<Integer> iterator { return elements.iterator; }

}

void testCollection() {
  value t1 = TestColl();
  //Test actual methods
  assert(t1.size == 0, "Collection.size 1");
  assert(t1.iterator.next() == exhausted, "Collection.iterator 1");
  //Now test inherited concrete methods
  assert(t1.empty, "Collection.empty 1");
  assert(!(1 in t1), "Collection.contains 1");
  assert(!t1.containsAny(1), "Collection.containsAny 1");
  assert(!t1.containsEvery(1), "Collection.containsEvery 1");
  assert(t1.count(1) == 0, "Collection.count 1");

  value t2 = TestColl(1,2,3,4,5,2);
  assert(t2.size == 6, "Collection.size 2");
  assert(t2.iterator.next() == 1, "Collection.iterator 2");
  assert(!t2.empty, "Collection.empty 2");
  assert(5 in t2, "Collection.contains 2");
  assert(t2.containsAny(0,3,10), "Collection.containsAny 2");
  assert(t2.containsEvery(2,3,4), "Collection.containsEvery 2");
  assert(t2.count(1) == 1, "Collection.count 2");
  assert(t2.count(2) == 2, "Collection.count 3");
  assert(t2.count(10) == 0, "Collection.count 4");
}
