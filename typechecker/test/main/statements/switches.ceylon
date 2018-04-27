void testSwitch(Integer i, String s, Character c, String? ss) {
    switch (i)
    case (-1) {}
    case (0) {}
    case (1) {}
    else {}
    
    switch (s)
    case ("") {}
    else {}
    
    switch (ss)
    case ("") {}
    case (null) {}
    else {}
    
    switch (c)
    case (' ') {}
    case ('\n') {}
    else {}
    
    $error switch (i)
    case (-1) {}
    case (0) {}
    case (-1) {}
    else {}
    
    $error switch (i)
    case (1) {}
    case (0) {}
    case (1) {}
    else {}
    
    $error switch (i)
    case (1k) {}
    case (0) {}
    case (1000) {}
    else {}
    
    $error switch (i)
    case (-1) {}
    case (0) {}
    case (1) {}
    
    $error switch (s)
    case ("") {}
    
    $error switch (s)
    case ("") {}
    case ("") {}
    else {}
    
    $error switch (ss)
    case ("") {}
    case (null) {}
    
    $error switch (c)
    case (' ') {}
    case ('\n') {}
    
    $error switch (c)
    case (' ') {}
    case ('\{#0020}') {}
    else {}
    
    $error switch (c)
    case ('\\') {}
    case ('\{#005C}') {}
    else {}
    
}

void fun0<X>(X x) {
    switch (x)
    case (null) {
        print(x);
    }
    else {
        print(x.string);
    }
}

void fun1<X>(X&Boolean x) {
    switch (x)
    //$error case (null) {
    //    print(x);
    //}
    case (true) {}
    case (false) {}
    else {
        $error print(x.string);
    }
}

void fun2<X>(X x) {
    switch (x)
    case (true) {
        print("T");
    }
    case (false) {
        print("F");
    }
    else {
        $error print(x.string);
    }
}

shared void switchOnvariable() {
    variable value int = 1;
    switch (int)
    case (1) {
        int = 2;
    }
    else {
        int = 3;
    }
}

void coverageWithTypeParameter<E>(String|Set<E> ss) {
    switch (ss)
    case (is String) {}
    case (is Set<E>) {}
}

void switchMixedCases() {
    
    Integer|String|Float|Null arg = null;
    switch(arg)
    case (Float) {
        $type:"Float" value a = arg;
    }
    case (String|1|2|3) {
        $type:"Integer|String" value a = arg;
    }
    else case (Integer|null) {
        $type:"Integer?" value a = arg;
    }
    
    $error switch(arg)
    case (Float) {
        $type:"Float" value a = arg;
    }
    case (String|1|2|3) {
        $type:"Integer|String" value a = arg;
    }
    case (Integer|null) {
        $type:"Integer?" value a = arg;
    }
    
    switch(arg)
    case (Float) {
        $type:"Float" value a = arg;
    }
    case (String|1|2|3) {
        $type:"Integer|String" value a = arg;
    }
    else case (Integer|null|"") {
        $type:"Null|String|Integer" value a = arg;
    }
    
    $error switch(arg)
    case (Float) {
        $type:"Float" value a = arg;
    }
    case (String) {
        $type:"String" value a = arg;
    }
    case (Integer|null|"") {
        $type:"Null|String|Integer" value a = arg;
    }
    
    $error switch(arg)
    case (Float) {
        $type:"Float" value a = arg;
    }
    case (String|1|2|3) {
        $type:"Integer|String" value a = arg;
    }
    case (null) {
    }
    
    Boolean? bool = null;
    switch (bool)
    case (true) {}
    case (false|Null) {}
    
    Object obj = "hello";
    switch (obj)
    case ("hello"|Integer) {
        $type:"String|Integer" value xx = obj;
    }
    else {}
    
    variable Object var = "hello";
    switch (var)
    case ("hello"|Integer) {
        $type:"Object" value xx = var;
    }
    else {}
    
    String|Integer thing = 1;
    switch (thing)
    case ("hello"|Integer) {
        $type:"String|Integer" value xx = thing;
    }
    else {
        $type:"String" value xx = thing;
    }
    
}

void valueCaseNarrowing(Object val, variable Object var) {
    switch (val)
    case ("foo"|"bar") {
        String fooOrBar = val;
    }
    case (is Integer) {}
    case ("baz"|Float) {
        String|Float strFloat = val;
    }
    else {}
    
    switch (var)
    case ("foo"|"bar") {
        $error String fooOrBar = var;
    }
    case (Integer) {}
    case ("baz"|Float) {
        $error String|Float strFloat = var;
    }
    else {}
}
