void bug1382() {
    variable value range = 0..2;
    value comp = { for (i in range) i };
    assert([0, 1, 2] == comp.sequence);
    range = -1..1;
    assert([-1, 0, 1] == comp.sequence);
}