void testTupleSwitch([Integer,String] pair, Object any) {
    switch (pair)
    case ([0,""]) {}
    case ([1,"foo"]) {}
    case ([1,"bar"]) {}
    else {}
    
    $error switch (pair)
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
    
    $error switch (any)
    case ([]) {}
    case ([0,""]) {}
    case ([0,"",'x']) {}
    case ([]) {}
    else {}
    
    $error switch (any)
    case ([0,""]) {}
    case ([0,""]) {}
    case ([0,"",'x']) {}
    else {}
    
    $error:"case is not disjoint"
    switch (pair)
    case ([0,""]) {}
    case ([Integer i, String s]) {}
    
    $error:"case is not disjoint"
    switch (pair)
    case ([0,""]) {}
    case (is [Integer, String]) {}
    
    switch (any)
    case ([1, true]) {}
    case ([2, true]) {}
    case ([1, false]) {}
    case ([1, null]) {}
    else {}
    
    $error switch (any)
    case ([1, true]) {}
    case ([2, true]) {}
    case ([1, false]) {}
    case ([1, true]) {}
    else {}
    
    $error switch (any)
    case ([1, null]) {}
    case ([1, true]) {}
    case ([2, true]) {}
    case ([1, false]) {}
    case ([1, null]) {}
    else {}
    
    switch (any)
    case ([true]) {}
    case ([false]) {}
    case ([null]) {}
    else {}
    
    $error switch (any)
    case ([true]) {}
    case ([true]) {}
    case ([null]) {}
    else {}
    
    Integer y = switch (pair = [1, 2]) case ([1, 1]) 1 case([0,0]) 0 else 2;
    
}

void testPatterns(
        [Integer,String] pair, 
        [Float,Float]|[Float,Float,Float] something, 
        <String->Object>|Null entry,
        Object any) {
    
    switch (pair)
    case ([Integer i, String s]) {
        $type:"Integer" value ii = i;
        $type:"String" value ss = s;
    }
    
    $error switch (pair)
    case ([Integer i, String s]) {}
    case ([String s, Integer i]) {}
    
    $error:"case is not disjoint"
    switch (pair)
    case ([Integer i, String s]) {}
    case ([Integer i, String s]) {}
    
    //$error switch (pair)
    //case ([Integer i, s]) {}
    
    switch (something)
    case ([Float x,Float y]) {
        $type:"Float" value xx = x;
        $type:"Float" value yy = y;
    }
    case ([Float x, Float y, Float z]) {
        $type:"Float" value xx = x;
        $type:"Float" value yy = y;
        $type:"Float" value zz = z;
    }

    switch (something)
    case ([Float x,Float* rest]) {
        $type:"Float" value xx = x;
        $type:"Float[]" value r = rest;
    }
    
    switch (entry)
    case (String key->Object item) {
        $type:"String" value k = key;
        $type:"Object" value i = item;
    }
    case (null) {}

    $error:"case is not disjoint"
    switch (entry)
    case (String key->Object item) {}
    case (is String->Object) {}
    case (null) {}
    
    $error:"case is not disjoint"
    switch (entry)
    case (is String->Object) {}
    case (String key->Object item) {}
    case (null) {}
    
    //switch (entry)
    //case (key -> item) {}
    //case (null) {}
    
    Float x 
            = switch ([Float, Float*] floats = [1.0, 2.0]) 
            case ([Float x, Float* ys]) x + (ys[0] else 0.0);
    
    Float y 
            = switch (coord = [1.0, 2.0]) 
            case ([Float x, Float y]) x^2 + y^2;
}