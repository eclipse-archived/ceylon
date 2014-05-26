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

class MiMatrix(Integer gridSize) satisfies Matrix<Integer> {
    value sb = [ for (i in 1..gridSize) [ for (j in 1..gridSize) j ] ];
    Matrix<Integer> grid = sb;
    shared actual Iterator<[Integer+]> iterator() { return grid.iterator(); }
    shared actual String string = grid.string;
    shared actual Integer hash = grid.hash;
    shared actual Boolean equals(Object other) => grid.equals(other);
    span = grid.span;
    segment = grid.segment;
    shared actual Matrix<Integer> reversed = grid.reversed;
    shared actual Integer lastIndex = grid.lastIndex;
    shared actual [Integer+]? get(Integer i) => grid[i];
    shared actual [Integer+][] rest = grid.rest;
    shared actual [Integer+] first = grid.first;
    shared actual Integer size => grid.size;
    shared actual Boolean contains(Object other) => grid.contains(other);
    last => grid.last;
    shared actual [Integer+][] spanTo(Integer to) =>
            to<0 then [] else span(0, to);

    shared actual [Integer+][] spanFrom(Integer from) =>
            span(from, size);
}

shared void issue225_1(Integer|String content){}
shared alias Issue225Alias => Integer|String;
shared void issue225_2(Issue225Alias content){}

void testAliasing() {
    print("testing type aliases");
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
}
