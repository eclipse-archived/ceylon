import assert {...}

class SubIdent1() extends IdentifiableObject(){}
class SubIdent2(Integer x) extends IdentifiableObject(){
  shared actual String string = "subident2";
  shared actual Boolean equals(Object other) {
    if (is SubIdent2 other) {
      return other.x == this.x;
    }
    return false;
  }
}

shared void testIdentifiable() {
  assert(SubIdent1().string.contains("@"), "SubIdent1.string");
  assert(!SubIdent2(0).string.contains("@"), "SubIdent2.string");
  assert(!(SubIdent1() === SubIdent1()), "SubIdent1 ===");
  assert(SubIdent1() != SubIdent1(), "SubIdent1 !=");
  assert(!(SubIdent2(1) === SubIdent2(1)), "SubIdent2 !==");
  assert(SubIdent2(1) == SubIdent2(1), "SubIdent2 ==");
}
