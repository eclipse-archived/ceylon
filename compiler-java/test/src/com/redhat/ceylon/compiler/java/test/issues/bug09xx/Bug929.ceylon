void bug929_m(Void... v) {
    
}
void bug929_1() {
    value s = "Hello World".characters;
    value x = " ".join(s[].lowercase.string);
}
void bug929_2(Character[] s={}) {
    value x= bug929_m(s[].integer);
}
void bug929_3(List<Character> s={}) {
    value x = bug929_m(s[].integer);
}
