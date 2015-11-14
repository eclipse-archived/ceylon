shared alias Strinteger => String|Integer;
shared alias Verbostring => String&[Character+];
alias Numbers => Integer[];
alias Objecton<T> given T satisfies Object => T|Singleton<T>;

shared interface Matrix<Cell> => List<List<Cell>>;

shared class LS<T>(Singleton<T>* ss) given T satisfies Object => LazyList<Singleton<T>>(ss);

shared Strinteger si = "5";

shared LS<Integer> ls = LS(Singleton(1));

shared abstract class Mimpl(Verbostring s) satisfies Matrix<String> {
  hash => 1;
  shared actual Boolean equals(Object other) => false;
}

shared class LazyList<out Elem>(shared {Elem*} elems) {
}