@noanno
interface I<out Element> {
    shared formal
    I<Result> map<Result>(Result collecting(Element element));
}
@noanno
interface J<out Element> of K satisfies I<Element> {
    shared formal actual
    J<Result> map<Result>(Result collecting(Element element));
}
@noanno
interface K of k satisfies J<Nothing> {
    shared actual
    K map<Result>(Result collecting(Nothing element)) => this;
}
@noanno
object k satisfies K {}