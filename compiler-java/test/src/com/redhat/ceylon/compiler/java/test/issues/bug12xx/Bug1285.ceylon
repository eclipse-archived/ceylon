@noanno
void bug1285() {
    value hash1 = 1.equals(2).hash;
    value hash2 = 1.0.equals(2.0).hash;
    value hash3 = true.equals(false).hash;
    value hash4 = 'a'.equals('b').hash;
    value hash5 = "".equals("").hash;
}
