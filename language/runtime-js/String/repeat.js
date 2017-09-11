function(times) {
    var x = this;
    var s = "";
    if (times<=0) return s;
    while (true) { 
        if (times & 1) { s += x; } 
        times >>= 1;
        if (times) { x += x; }
        else { break; }
    } 
    return s; 
}
