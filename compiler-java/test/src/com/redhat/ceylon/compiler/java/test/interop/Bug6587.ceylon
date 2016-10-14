import javax.persistence{
    EntityManager,
    persistenceContext
}
import javax.enterprise.inject {produces}

class Bug6587() {
    produces persistenceContext
    late EntityManager lateEntityManager;
    
    produces persistenceContext
    variable EntityManager variableEntityManager = nothing;
    
    produces persistenceContext
    EntityManager entityManager = nothing;
    
    produces persistenceContext
    EntityManager transientEntityManager => nothing;
    
    produces persistenceContext
    shared EntityManager assignableEntityManager => nothing;
    assign assignableEntityManager {}
    
    produces persistenceContext
    shared EntityManager sharedEntityManager = nothing;
}
/*
class Bug65872() {
    
    produces 
    //persistenceContext
    EntityManager assignableEntityManager => nothing;
    assign assignableEntityManager {}
    
}*/