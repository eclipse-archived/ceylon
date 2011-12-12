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
        @type["Correspondence<Integer,String>&Sized"] value cc = c;
    }
    
    Correspondence<Integer,String> d = strings;
    if (is Sized&Container d) {
        String? s = d[0];
        Integer size = d.size;
        Boolean empty = d.empty;
        @type["Correspondence<Integer,String>&Sized"] value dd = d;
    }

    Correspondence<Integer,String> e = strings;
    if (is Sized&Iterable<String> e) {
        String? s = e[0];
        Integer size = e.size;
        Boolean empty = e.empty;
        for (String str in e) {} 
        @type["Correspondence<Integer,String>&Sized&Iterable<String>"] value ee = e;
    }

}