import ceylon.language { print }

void infix() {
    print("hello" contains "hell");
    print("hello" collect (Character c) => c.hash);
    value s = "hello";
    print(s contains "hell");
    print(s collect (Character c) => c.hash);
    (s.uppercased contains "HELL").equals(true);
    value x = $101010 xor $111000;
    print(x);
}
