<Any> => Any() valueTc {
    return nothing;
}
        
void consumesTc(<Any> => Any() tc) {
    Anything a = tc();
}

<Any> => Any() producesTc() {
    return nothing;
}


class ConsumesTc(<Any> => Any() tc) {
    Anything a = tc();
    
    shared <Any> => Any() valueTc {
        return nothing;
    }
    
    shared void consumesTc(<Any> => Any(Any) tc) {
        Anything a = tc("");
    }
    
    shared <Any> => Any() producesTc() {
        return nothing;
    }
    
    void use(ConsumesTc other) {
        <Any> => Any(Any) pipeRef = identity;
        this.consumesTc(identity);
        other.consumesTc(identity);
    }
}

/*
alias X<Any> => Any();

class ConstrainedTc<F>(F tc) 
        given F satisfies X {
    // note: syntax error if type constraint tries to satisfy a type constructor
    Anything a = tc();
}*/

// TC in union and intersection