@noanno
void bug1207() {
    // these ones are optimised down to regular calls
    (bug1207)();
    ("hello".replace) ("h", "H");
    
    // these ones create callables
    value c1 = (1 == 2 then bug1207 else bug1207);
    (1 == 2 then bug1207 else bug1207)();
    value f = ("hello".replace);
}