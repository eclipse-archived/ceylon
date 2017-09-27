import ceylon.interop.persistence {
        EntityManager
}

shared void run(){
    EntityManager entityManager = nothing;
    entityManager.find(`Integer`, 2);
}
