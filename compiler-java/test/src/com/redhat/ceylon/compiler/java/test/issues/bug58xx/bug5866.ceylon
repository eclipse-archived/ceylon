void bug5866() {
    try {
        String string;
        for(value ignore in [finished])
        {
            string = "";
            break;
        }
        print(string.size);
    } catch (AssertionError e) {
        if (e.message != "nonempty Iterable with initial 'finished' element") {
            throw;
        }
    }
    
    //value a = [finished, 1, finished];
    //value [b, *c] = a;
    //print(c[1].string);
}
