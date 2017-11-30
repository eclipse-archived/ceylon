import javax.persistence{
    EntityManager,
    persistenceContext
}
import javax.enterprise.inject {produces}
import javax.enterprise.context {requestScoped}

produces requestScoped persistenceContext
late EntityManager bug6589;