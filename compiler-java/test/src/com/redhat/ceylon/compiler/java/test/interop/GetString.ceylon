class GetString() {
    value jgs = JavaGetString();
    variable String s = jgs.string;
    s = jgs.getString();
    variable Boolean b = jgs.isString();
    jgs.setString("");
    
    variable Integer h = jgs.hash;
    h = jgs.getHash();
    b = jgs.isHash();
    jgs.setHash(1);
}