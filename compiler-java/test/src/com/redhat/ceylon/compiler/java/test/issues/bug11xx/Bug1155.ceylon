@noanno
class Bug1155() {
    shared String narySequenced(
    String s1, String* s) 
        => "narySequenced(``s1``; ``s``)";
}
@noanno
void bug1155() {
    value b = Bug1155();
    String(String, String, String*) r = b.narySequenced;
    print(r("nary1", "rest1"));
    print(r("nary1", "rest1", "rest2"));
}