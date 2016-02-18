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

shared class C5904 {
    shared Integer i;
    shared new (Integer a) {
        this.i = a;
    }
    shared new create(Integer a, Integer b) {
        this.i = a + b;
    }
}

shared class CAlias5904(Integer x) => C5904(x);
shared class CCreateAlias5904(Integer x, Integer y) => C5904.create(x, y);

interface I5904 {
    shared default String id => "I";
}

interface IAlias5904 => I5904;
interface IAliasAlias5904 => IAlias5904;

class Class5904() satisfies I5904 {
    shared actual String id => "C";
    shared String id_i => super.id;
    shared String id_iAlias => (super of IAlias5904).id;
    shared String id_iAliasAlias => (super of IAliasAlias5904).id;
}

shared class C5903 {
    shared Integer i;
    shared new (Integer a) {
        this.i = a;
    }
    shared new create(Integer a, Integer b) {
        this.i = a + b;
    }
}

shared class CAlias5903(Integer x) => C5903(x);
shared class CCreateAlias5903(Integer x, Integer y) => C5903.create(x, y);

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

    //nested aliases
    Anything c = CAlias5904(10);
    Anything cc = CCreateAlias5904(10, 20);

    check((c of Anything) is C5904, "#5904.1"); // ok
    check((c of Anything) is CAlias5904, "#5904.2"); // ok
    check((c of Anything) is CCreateAlias5904, "#5904.3"); // TypeError: Cannot read property 'T$name' of undefined
    check((cc of Anything) is C5904, "#5904.4"); // ok
    check((cc of Anything) is CAlias5904, "#5904.5"); // ok
    check((cc of Anything) is CCreateAlias5904, "#5904.6"); // TypeError: Cannot read property 'T$name' of undefined

    value c2 = Class5904();

    check(c2.id == "C", "#5904.7");
    check(c2.id_i == "I", "#5904.8");
    check(c2.id_iAlias == "I", "#5904.9");
    check(c2.id_iAliasAlias == "I", "#5904.10");

    check((c2 of Anything) is I5904, "#5904.11");
    check((c2 of Anything) is IAlias5904, "#5904.12"); // error
    check((c2 of Anything) is IAliasAlias5904, "#5904.13"); // error

    check(!("" of Anything) is I5904, "#5904.14");
    check(!("" of Anything) is IAlias5904, "#5904.15"); // error
    check(!("" of Anything) is IAliasAlias5904, "#5904.16"); // error
    check(CAlias5903(10).i == 10, "#5903.1");
    check(CAlias5903.create(10, 20).i == 30, "#5903.2");
    check(CCreateAlias5903(10, 20).i == 30, "#5903.3");
    check(CCreateAlias5903.create(10, 20).i == 30, "#5903.4");
}
