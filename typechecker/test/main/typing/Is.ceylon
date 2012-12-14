class Is() {
    
    interface SimpleContainer<T> 
        given T satisfies Number {}
    
    String[] strings = { "hello", "world" };
    
    if (is Sequence<String> strings) {
        print(strings.first);
    }
    @error if (is Sequence strings) {}
    @error if (is SimpleContainer<String> strings) {}
    @error if (is Is.SimpleContainer strings) {}
    @error if (is Is.SimpleContainer<String> strings) {}

    if (strings is Sequence<String>) {
        //print(strings.first);
    }
    @error if (strings is Sequence) {}
    @error if (strings is SimpleContainer<String>) {}
    @error if (strings is Is.SimpleContainer) {}
    @error if (strings is Is.SimpleContainer<String>) {}

    void method<T>() {
        if (is T strings) {}
        if (strings is T) {}
    }
    
    Correspondence<Integer,String> c = strings;
    if (is Sized c) {
        String? s = c[0];
        Integer size = c.size;
        Boolean empty = c.empty;
        @error if ("hello" in c) {}
        //@error for (String str in c) {}
        @type:"Correspondence<Integer,String>&Sized" value cc = c;
    }
    Correspondence<Integer,String> d = strings;
    if (is Sized&Container<Void> d) {
        String? s = d[0];
        Integer size = d.size;
        Boolean empty = d.empty;
        @error if ("hello" in d) {}
        //@error for (String str in d) {}
        @type:"Correspondence<Integer,String>&Sized" value dd = d;
    }
    
    Correspondence<Integer,String> e = strings;
    if (is Sized&Iterable<String> e) {
        String? s = e[0];
        Integer size = e.size;
        @error Boolean empty = e.empty;
        @error if ("hello" in e) {}
        for (String str in e) {}
        @type:"Correspondence<Integer,String>&Sized&Iterable<String>" value ee = e;
    }
    if (is Sized&Category e) {
        String? s = e[0];
        Integer size = e.size;
        Boolean empty = e.empty;
        if ("hello" in e) {}
        //@error for (String str in e) {} 
        @type:"Correspondence<Integer,String>&Sized&Category" value ee = e;
    }
    if (is Sized&{String...} e) {
        String? s = e[0];
        Integer size = e.size;
        @error Boolean empty = e.empty;
        @error if ("hello" in e) {}
        for (String str in e) {} 
        @type:"Correspondence<Integer,String>&Sized&Iterable<String>" value ee = e;
    }
    if (is String[] e) {
        String? s = e[0];
        Integer size = e.size;
        Boolean empty = e.empty;
        if ("hello" in e) {} 
        for (String str in e) {}
        @type:"Sequential<String>" value ee = e;
    }
    
    Boolean b1 = is Sized&Iterable<String> e;
    Boolean b2 = is Sized|Category e;
    Boolean b3 = is Sized&{String...} e;
    Boolean b4 = is <Sized|Category>&Iterable<Object> e;
    Boolean b5 = is [String...] e;
    Boolean b6 = is String[] e;
    Boolean b7 = is [String,Integer] e;
    Boolean b8 = is String() e;
    Boolean b9 = is String(Integer) e;
    Boolean b10 = is String? e;
    Boolean b11 = is <Sized|Category> e;
    
    Boolean c1 = e is Sized&Iterable<String>;
    Boolean c2 = e is Sized|Category;
    Boolean c3 = e is Sized&{String...};
    Boolean c4 = e is <Sized|Category>&Iterable<Object>;
    Boolean c5 = e is [String...];
    Boolean c6 = e is String[];
    Boolean c7 = e is [String,Integer];
    Boolean c8 = e is String();
    Boolean c9 = e is String(Integer);
    Boolean c10 = e is String?;
    Boolean c11 = e is <Sized|Category>;
    
    String? s = null;
    
    switch (s)
    case (is String) {
        process.writeLine(s);
    }
    case (null) {}

    switch (s)
    case (is String) {
        process.writeLine(s);
    }
    else {}
    
    void m(String s) {
        @error if (is String s) { }
        @error if (is Integer s) { }
        @error value b = is String s;
        @error value c = is Integer s;
    }
    
    value next = "hello".iterator.next();
    if (!is Finished next) {
        Character char = next;
    }
    if (!is Finished ch = next) {
        Character char = ch;
    }
    @error if (!is Object next) {}
    @error if (!is Nothing next) {}
    
    Identifiable? i = null;
    if (is Category cat = i) {
        Identifiable&Category ic = cat;
    }
    
    void notIs<T>(T... ts) {
        value next = ts.iterator.next();
        if (!is Finished next) {
            T tt = next;
        }
        if (!is Finished t = next) {
            T tt = t;
        }
        @error if (!is T next) {
            //Finished f = next;
        }
        @error if (!is T t = next) {
            //Finished f = t;
        }
    }
}