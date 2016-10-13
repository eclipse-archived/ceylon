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
    case ([]) {}
    case ([0,""]) {}
    case ([0,"",'x']) {}
    else {}
    
    @error switch (any)
    case ([]) {}
    case ([0,""]) {}
    case ([0,"",'x']) {}
    case ([]) {}
    else {}
    
    @error switch (any)
    case ([0,""]) {}
    case ([0,""]) {}
    case ([0,"",'x']) {}
    else {}
    
    @error switch (pair)
    case ([0,""]) {}
    case ([Integer i, String s]) {}
    
    @error switch (pair)
    case ([0,""]) {}
    case (is [Integer, String]) {}
    
    switch (any)
    case ([1, true]) {}
    case ([2, true]) {}
    case ([1, false]) {}
    case ([1, null]) {}
    else {}
    
    @error switch (any)
    case ([1, true]) {}
    case ([2, true]) {}
    case ([1, false]) {}
    case ([1, true]) {}
    else {}
    
    @error switch (any)
    case ([1, null]) {}
    case ([1, true]) {}
    case ([2, true]) {}
    case ([1, false]) {}
    case ([1, null]) {}
    else {}
    
}