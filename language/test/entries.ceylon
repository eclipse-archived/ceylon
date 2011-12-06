Entry<Natural,String> entry {
    item="hello";
    key=1;
}

void entriesAndRanges() {
    
    Entry<Equality, Equality> e = entry;
    assert(e.string=="1->hello", "entry string");
    assert(e.key==1, "entry key");
    assert(e.item=="hello", "entry item");
    assert(entry==1->"hello", "entry equals");
    assert(entry!=2->"hello", "entry equals");
    assert(entry!=1->"bye", "entry equals");
    
}