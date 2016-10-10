void testTupleSwitch([Integer,String] pair, Object any) {
    switch (pair)
    case ([0,""]) {}
    case ([1,"foo"]) {}
    case ([1,"bar"]) {}
    else {}
    
    @error switch (pair)
    case ([0,""]) {}
    case ([1,"foo"]) {}
    case ([1,"bar"]) {}
    case ([1,"foo"]) {}
    else {}
    
    switch (any)
    case ([0,""]) {}
    case ([0,"",'x']) {}
    else {}
    
    @error switch (any)
    case ([0,""]) {}
    case ([0,""]) {}
    case ([0,"",'x']) {}
    else {}
}