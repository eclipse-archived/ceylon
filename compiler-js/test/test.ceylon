void m() {
void printIf1(String? s) {
    if (exists s) {
        print(s);
    } else {
        print("Nothing to print.");
    }
}
String? s1 = null;
String? s2 = "I do exist";
printIf1(s1);
printIf1(s2);
print(s1 else "Nothing here...");
print(s2 else "Nothing here...");

}
