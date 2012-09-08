void assertions() {
    Void name = "gavin";
    assert (exists o = name);
    assert (o=="gavin");
    assert (is String n = name);
    print(n.uppercased);
}