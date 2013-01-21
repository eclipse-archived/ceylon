import check {...}

class TestCorresp() satisfies Correspondence<Integer, String> {

  shared actual String? item(Integer key) {
    if (key > 0 && key <= 10) {
      return "ITEM " key "";
    }
    return null;
  }

}

void testCorrespondence() {
  value t = TestCorresp();
  check(t.defines(1), "Correspondence.defines 1");
  check(!t.defines(11), "Correspondence.defines 2");
  check(t.definesEvery{1,2,3,4,5,6,7,8,9,10}, "Correspondence.definesEvery");
  check(!t.definesEvery{2,4,6,8,10,12}, "!Correspondence.definesEvery");
  check(t.definesAny{30,20,10,0}, "Correspondence.definesAny");
  check(!t.definesAny{20,30,40,50}, "!Correspondence.definesAny");
  value items = t.items{1,3,5,7,9};
  check(items.size == 5, "Correspondence.items 1");
  check(items.defines(4), "Correspondence.items 2");
  //TODO  keys
  //check(t.keys.containsEvery(1,2,3,4,5,6,7,8,9,10), "Correspondence.keys");
}
