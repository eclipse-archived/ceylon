alias Strinteger = String|Integer;
alias Verbostring = String&Some<Character>;
alias Numbers = Integer[];
alias Objecton<T> given T satisfies Object = T|Singleton<T>;

shared interface Matrix<Cell> = Sequence<Sequence<Cell>>;

shared class LS<T>(Singleton<T>... ss) given T satisfies Object = LazyList<Singleton<T>>;

shared Strinteger si = "5";

shared LS<Integer> ls = LS(Singleton(1));

shared abstract class Mimpl(Verbostring s) satisfies Matrix<String> {
}
