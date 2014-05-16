class Optional() {
    
    class X() {}
    class Y() {}
    X? x = X();
    X? y = null;
    X? z = x;
    X? w = y;
    
    void xx(X x) {}
    
    if (exists x) {
        xx(x);
    }
    
    if (exists X xxx = x) {
        xx(xxx);
    }
    
    if (exists @error Y xxx = x) {}
    
    value sx = [ X() ];
    value sxn = [ X(), null ];
    value sy = [ Y() ];
    value syn = [ Y(), null ];
    value sxy = [ X(), Y() ];
    variable <X|Y|Null>[] ss = [ X(), Y(), null ];
    ss=sx;
    ss=sy;
    ss=sxy;
    ss=sxn;
    ss=syn;
    value bs = [ X(), "foo" ];
    @error ss=bs;
    
    class Foo<T>() {
        shared T? optional = null;
        shared T definite { throw; }
        shared T[]? optionalList { throw; }
        shared T[] list { throw; }
    }
    
    String? optional = Foo<String>().optional;
    String definite = Foo<String>().definite;
    String[]? optionalList = Foo<String>().optionalList;
    String[] list = Foo<String>().list;
    
    @error String sssss = list.first;
    @error Integer nnnn = list.lastIndex;
    
    if (nonempty list) {
        String s = list.first;
        Integer li = list.lastIndex;
    }
    
    if (nonempty Sequence<String> strings = optionalList) {
        String s = strings.first;
        Integer li = strings.lastIndex;
    }
    
    Sequence<String> stuff = [ "foo" ];
    Character[][] chars = stuff*.sequence;

    String[] nostuff = {};
    Character[][] nochars = nostuff*.sequence;
    
    String? maybestuff = null;
    Character[]? maybechars = maybestuff?.sequence;
    
    Character[] somechars = {};
    Integer scs = somechars.size;
    value sci = somechars.iterator;
    
    T t<T>(T t) => t;
    
    @type:"Null|String|String|Null|String|Integer|Sequence<Object>" 
    String? | String | String? | Integer | Sequence<Object> foobar1 = -1;
    @type:"Null|String|Integer|Sequence<Object>" value foobar1a = foobar1;
    @type:"Null|String|Integer|Sequence<Object>" t(foobar1);
    String|Integer|Sequence<Object>|Null foobar1b = foobar1;
    
    @type:"Null|Sequential<String>|Sequential<String>|Sequence<String>|Integer|Float" 
    String[]? | String[] | Sequence<String> | Integer | Float foobar2 = 1.float;
    @type:"Null|Sequential<String>|Integer|Float" value foobar2a = foobar2;
    @type:"Null|Sequential<String>|Integer|Float" t(foobar2);
    Sequential<String>|Integer|Float|Null foobar2b = foobar2;
    
    @type:"Sequence<Null|String|Integer|Sequence<Object>|Sequential<String>|Float>" 
    value xyz1 = [ foobar1, foobar2 ].sequence;
    @type:"Sequential<Null|String|Integer|Sequence<Object>|Sequential<String>|Float>" 
    value xyz2 = { foobar1, foobar2 }.sequence;
    
    //TODO: I think the type parameter X does
    //      not hide the X defined above - it
    //      should!
    /*shared void entries<X>(X* sequence) 
            given X satisfies Equality {
        if (nonempty sequence) {
            entries<X>(sequence.clone);
        }
    }*/
    
    class WithOptional<T>(val) {
        shared T? val;
        shared T[]? seq;
        if (exists val) {
            seq = [val];
        }
        else {
            seq = null;
        }
    }
    
    if (exists s = WithOptional<String>("hello").val) {
        @type:"String" value es = s;
    }
    
    if (exists seq = WithOptional<String>("goodbye").seq) {
        @type:"Sequential<String>" value sseq = seq;
    }
    
    if (nonempty seq = WithOptional<String>("hello again").seq) {
        @type:"Sequence<String>" value sseq = seq;
    }
    
    class WithOptionalString(String? val)
            extends WithOptional<String>(val) {
        
        if (exists val) {
            @type:"String" value ss = val;
        }
        
        void method() {
        
            if (exists seq = WithOptionalString("hello").seq) {
                @type:"Sequential<String>" value sseq = seq;
            }
            
            if (nonempty seq = WithOptionalString("hello").seq) {
                @type:"Sequence<String>" value sseq = seq;
            }
            
        }
        
    }
    
    
    if (exists seq = WithOptionalString("hello").seq) {
        @type:"Sequential<String>" value sseq = seq;
    }
    
    if (nonempty seq = WithOptionalString("hello").seq) {
        @type:"Sequence<String>" value sseq = seq;
    }
    
}