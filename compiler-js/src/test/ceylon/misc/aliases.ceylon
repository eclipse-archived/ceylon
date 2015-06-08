import check { ... }

shared alias Strinteger => String|Integer;

shared class Test284(Strinteger x) {
  print(x);
}

shared class AliasingClass() {
    shared interface AliasingIface {
        shared Boolean aliasingIface() { return true; }
    }
    shared class AliasingInner() {
        shared Boolean aliasingInner() { return true; }
    }
}

class AliasingSubclass() extends AliasingClass() {
    shared class InnerAlias() => AliasingInner();
    shared class SubAlias() extends InnerAlias() {}

    shared Boolean aliasingSubclass() {
        return SubAlias().aliasingInner();
    }
    shared interface AliasedIface => AliasingIface;
}

class AliasingSub2() extends AliasingSubclass() {
    shared AliasedIface iface {
        object aliased satisfies AliasedIface {
        }
        return aliased;
    }
}

interface Matrix<Cell> => Sequence<[Cell+]>;
class Listleton<T>(List<T> l) => Singleton<List<T>>(l);

class MiMatrix(Integer gridSize) satisfies List<[Integer+]> {
    value sb = [ for (i in 1..gridSize) [ for (j in 1..gridSize) j ] ];
    Matrix<Integer> grid = sb;
    shared actual Iterator<[Integer+]> iterator() { return grid.iterator(); }
    shared actual String string = grid.string;
    shared actual Integer hash = grid.hash;
    shared actual Boolean equals(Object other) => grid.equals(other);
    span = grid.span;
    measure = grid.measure;
    shared actual Matrix<Integer> reversed = grid.reversed;
    shared actual Integer lastIndex = grid.lastIndex;
    shared actual [Integer+]? getFromFirst(Integer i) => grid.getFromFirst(i);
    shared actual [Integer+][] rest = grid.rest;
    shared actual [Integer+] first = grid.first;
    shared actual Integer size => grid.size;
    shared actual Boolean contains(Object other) => grid.contains(other);
    last => grid.last;
    shared actual [Integer+][] spanTo(Integer to) =>
            to<0 then [] else span(0, to).sequence();

    shared actual [Integer+][] spanFrom(Integer from) =>
            span(from, size).sequence();
}

shared void issue225_1(Integer|String content){}
shared alias Issue225Alias => Integer|String;
shared void issue225_2(Issue225Alias content){}

shared class Issue412() {
  alias Boom => String;
  List<Boom> boom = Singleton<Boom>("BOOM!");
  shared Object? bleh => boom.first;
}

class Issue519<A,B>(shared A key, shared B item)
    given A satisfies Object {
  alias Test => A->B;
  Object c=key->item;
  check(c is Test, "#519");
}

//class Alias563(Integer i=42) => Integer(i);

void testAliasing() {
    check(AliasingSubclass().aliasingSubclass(), "Aliased member class");
    class InnerSubalias() => AliasingSubclass();
    check(InnerSubalias().aliasingSubclass(), "Aliased top-level class");
    interface AliasedIface2 => AliasingClass.AliasingIface;
    Boolean use(AliasedIface2 aif) { return aif.aliasingIface(); }
    check(use(AliasingSub2().iface), "Aliased member interface");
    Strinteger xxxxx = 5;
    check(xxxxx is Integer, "Type alias");
    check(Listleton([[1],[2],[3]].first).string=="[[1]]", "class alias ``Listleton([[1],[2],[3]].first)`` instead of [ [ 1 ] ]");
    check(MiMatrix(2).string=="[[1, 2], [1, 2]]", "interface alias ``MiMatrix(2)`` instead of [[1, 2], [1, 2]]");
    Object xxxxx1 = 6;
    Object xxxxx2 = "XXXX";
    check(xxxxx1 is String|Integer, "is String|Integer");
    check(xxxxx2 is String&List<Anything>, "is String&List");
    function cualquiera(Boolean* bits) => any(bits);
    check(cualquiera(true,true,true), "seq arg method alias");
    check(Issue412().bleh exists, "Issue 412");
    Issue519(1, "2");
    //check(Alias563()==42, "#563");
}
