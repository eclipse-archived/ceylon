void exceptionSuppressed() {
    value sup = Exception("suppressed");
    value nonsup = Exception("overriding");
    nonsup.addSuppressed(sup);
    
    variable Throwable[] x = nonsup.suppressed;
    value ref = Throwable.suppressed;
    assert([sup] == ref(nonsup));
    
    assert([sup] == [nonsup]*.suppressed[0]);
    
    Throwable? t = nonsup;
    assert([sup] == (t?.suppressed else []));
}