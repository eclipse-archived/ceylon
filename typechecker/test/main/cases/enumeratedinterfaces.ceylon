interface Anything of Something | SomethingElse {}
interface Something satisfies Anything {
    shared formal String something;
}
interface SomethingElse satisfies Anything {
    shared formal Object somethingElse;
}

void switchAnything(Anything any) {
    switch (any)
    case (is Something) { 
        print(any.something); 
    }
    case (is SomethingElse) { 
        print(any.somethingElse); 
    }
}
