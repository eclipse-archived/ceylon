void assertions() {
    Void name = "gavin";
    doc "name is required" 
    assert (exists o = name);
    assert (o=="gavin");
    doc "name must be a string"
    assert (is String n = name);
    print(n.uppercased);
}