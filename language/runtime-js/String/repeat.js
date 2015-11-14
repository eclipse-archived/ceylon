function(times) {
    var x = this;
    var s = "";
    while (true) { 
        if (times & 1) { s += x; } 
        times >>= 1;
        if (times) { x += x; }
        else { break; }
    } 
    return s; 
}
