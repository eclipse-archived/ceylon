import check {...}

class SubIdent1() extends Basic(){}
class SubIdent2(Integer x) extends Basic(){
  shared actual String string = "subident2";
  shared actual Boolean equals(Object other) {
    if (is SubIdent2 other) {
      return other.x == this.x;
    }
    return false;
  }
}

shared void testIdentifiable() {
  check(SubIdent1().string.contains("@"), "SubIdent1.string");
  check(!SubIdent2(0).string.contains("@"), "SubIdent2.string");
  check(!(SubIdent1() === SubIdent1()), "SubIdent1 ===");
  check(SubIdent1() != SubIdent1(), "SubIdent1 !=");
  check(!(SubIdent2(1) === SubIdent2(1)), "SubIdent2 !==");
  check(SubIdent2(1) == SubIdent2(1), "SubIdent2 ==");
}
