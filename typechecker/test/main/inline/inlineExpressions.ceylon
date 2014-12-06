void inlineExpressions(Integer? arg, Boolean bool, Integer|Float num) {
    
    @type:"String" value numStr = 
            if (num==0) then "zero" 
            else if (num==1) then "unit" 
            else num.string;
    
    @type:"Integer|Float" value someStuff 
            = if (exists arg) 
                then arg else 0.0;
    
    @type:"Float" value otherStuff 
            = if (exists arg) 
                then 1.0 else 0.0;
    
    @type:"String" value moreStuff 
            = switch (bool) 
                case (true) "bar" 
                case (false) "foo";
    
    @type:"Boolean|Null" value someMoreStuff 
            = switch (bool) 
                case (true) true 
                case (false) null;

    @error value moreStuffBroken
            = switch (bool) 
                case (true) "bar";
    
    @type:"Integer|Float" value evenMoreStuff 
            = switch (num) 
                case (is Integer) -num 
                case (is Float) num/2.0;
    
    @error value evenMoreStuffBroken
            = switch (num) 
                case (is Integer) -num;
    
    @type:"Basic&Category<String>" value xxx 
            = object extends Basic() 
            satisfies Category<String> {
        contains(String that) => !that.empty;
    };

    print(switch (bool) case (true) "bar" case (false) "foo");
    
    @type:"String|Float" value a1 = 
            if (exists arg) then arg.string else 0.0;
    @type:"String" value s1 
            = switch (arg) case (is Null) "null" case (is Integer) arg.string;
    
    @type:"String|Float" value a2 = 
            if (exists a=arg) then a.string else 0.0;
    @type:"String" value s2 
            = switch (a=arg) case (is Null) "null" case (is Integer) a.string;
    
    print(object satisfies Category<String> {
        print("creating object");
        contains(String that) => !that.empty;
    });
    
    print { val = if (exists arg) then arg else 0.0; };
    print { if (exists arg) then arg else 0.0; };
    print { val = switch (bool) case (true) "bar" case (false) "foo"; };
    print { switch (bool) case (true) "bar" case (false) "foo"; };
    print { 
        object extends Object() 
                satisfies Category<String> {
            contains(String that) => !that.empty;
            equals(Object that) => false;
            hash => 0;
        };
    };
    print { 
        val = object extends Object() 
                satisfies Category<String> {
            contains(String that) => !that.empty;
            equals(Object that) => false;
            hash => 0;
        };
    };
    @type:"Tuple<Basic,Basic,Tuple<Basic,Basic,Empty>>" value objs1 = [object{}, object{}];
    @type:"Array<Basic>" value objs2 = Array {object{}};
}

void run(String? string) {
    String val1 = if (!exists string) 
            then "hello" else string;
    String val2 = switch (string) 
            case (is Null) "hello" else string;
}

void switchOnTypeParameter<T>(T t) {
    switch (Anything ref = t)
    case (null) {}
    else { print(ref.string); }
    
    switch (Anything ref = t)
    case (null) {}
    case (is Object) { print(ref.string); }
    
    switch (t)
    case (is Null) {}
    else { print(t.string); }
    
    if (is Null t) {}
    else { print(t.string); }
    
    switch (t)
    case (is Null) {}
    case (is Object) { print(t.string); }
    
    if (!exists t) {}
    else { print(t.string); }
}

void scopeContainingLets() {
    value x = let(y=1) y;
    value z = let(y=1) y;
    value w = let(y=1, @error y=3) y;
    value u = let(@error u=1) u;
}

void scopeContainingSwitchesAndConditions(Object? obj) {
    value x1 = if (exists o=obj) then obj else "";
    value x2 = if (exists o=obj) then obj else "";
    value z1 = switch (o=obj) case (null) 1 case (is Object) 2;
    value z2 = switch (o=obj) case (is Null) 1 else 2;
}

void destructuringLet([String, Float, Integer] tuple, String->Object entry) {
    value x1 = let ([s, f, i]=tuple) s.size + f*i;
    value y2 = let ([String s, Float f, Integer i]=tuple) s.size + f*i;
    value z3 = let ([s, @error Integer f, Integer i]=tuple) s.size + f*i;
    value e1 = let (k->v=entry) k+v.string;
    value f2 = let (String k->Object v=entry) k+v.string;
    value g3 = let (String k->@error String v=entry) k+v;
}