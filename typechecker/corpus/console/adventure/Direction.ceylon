doc "The set of directions in which the
     player can go."
see (Connection)
shared class Direction() 
        of north | south | east | west | up | down 
        extends Case() {}

shared object north extends Direction() {}
shared object south extends Direction() {}
shared object east extends Direction() {}
shared object west extends Direction() {}
shared object up extends Direction() {}
shared object down extends Direction() {}

