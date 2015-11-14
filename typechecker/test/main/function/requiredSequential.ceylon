T maxim<T>(T+ ts) 
        given T satisfies Comparable<T> 
        => max(ts);

void requiredSequential() {
    @type:"Integer" value integer = maxim(1, 2, 3);
    @type:"String" value string = maxim("", " ");
    @error maxim();
    @error maxim(*[]);
    maxim(*[1.0]);
    @error maxim(for (x in [""]) if (x!="") x);
    maxim("", for (x in [""]) if (x!="") x);
    maxim(for (x in [""]) x);
    String(String+) fun = maxim<String>;
    @error String(String*) fun2 = maxim<String>;
    @error fun();
    fun("");
    @error fun(*[]);
    fun(*["x"]);
    @error fun(for (x in [""]) if (x!="") x);
    fun("", for (x in [""]) if (x!="") x);
    fun(for (x in [""]) x);
    maxim { ts = [3.0]; };
    maxim { @error ts = []; };
}