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
    
    try {
        value a = [finished, finished, finished];
        value [b, *c] = a; // CCE
    } catch (AssertionError e) {
        if (e.message != "length of c is less than minimum length of its static type Finished[2]") {
            throw;
        }
    }
    
    try {
        value a = [finished, 1, finished];
        value [b, *c] = a;
        print(c[1].string);
    } catch (AssertionError e) {
        if (e.message != "length of c is less than minimum length of its static type [Integer, Finished]") {
            throw;
        }
    }
}
