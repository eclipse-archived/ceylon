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
    
    Correspondence<Natural,String> c = strings;
    if (is Sized c) {
        String? s = c[0];
        Natural size = c.size;
        Boolean empty = c.empty;
        @type["Correspondence<Natural,String>&Sized"] value cc = c;
    }
    
    Correspondence<Natural,String> d = strings;
    if (is Sized&Container d) {
        String? s = d[0];
        Natural size = d.size;
        Boolean empty = d.empty;
        @type["Correspondence<Natural,String>&Sized"] value dd = d;
    }

    Correspondence<Natural,String> e = strings;
    if (is Sized&Iterable<String> e) {
        String? s = e[0];
        Natural size = e.size;
        Boolean empty = e.empty;
        for (String str in e) {} 
        @type["Correspondence<Natural,String>&Sized&Iterable<String>"] value ee = e;
    }

}