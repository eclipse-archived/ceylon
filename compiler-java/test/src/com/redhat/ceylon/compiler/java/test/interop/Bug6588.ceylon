import javax.persistence{
    EntityManager,
    persistenceContext
}
import javax.enterprise.inject {produces}

class Bug65872() {
    
    produces 
    //persistenceContext
    EntityManager assignableEntityManager => nothing;
    assign assignableEntityManager {}
    
}