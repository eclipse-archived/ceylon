interface ReadList<out T> {
    shared formal T get(Integer i);
}

class Tricky() satisfies ReadList<ReadList<Tricky>> {
    shared actual ReadList<Tricky> get(Integer i) {
        throw;
    }
}

Tricky|ReadList<Tricky> tricky(Integer i) { 
    if ( i <= +0) {
        return Tricky(); 
    } 
    else { 
        return tricky(i-1).get(i); 
    }
}