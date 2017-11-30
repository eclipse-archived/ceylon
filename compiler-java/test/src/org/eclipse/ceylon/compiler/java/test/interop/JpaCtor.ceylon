shared abstract class JpaCtorWithoutNullarySub<X,Y,Z> extends JpaCtorWithoutNullary {
    shared new (String s) extends JpaCtorWithoutNullary(s){
        
    }
    shared new clone(JpaCtorWithoutNullary other) extends JpaCtorWithoutNullary(other) {
        
    }
}
shared class JpaCtorWithoutNullarySubSub<X,Y,Z> extends JpaCtorWithoutNullarySub<X,Y,Z> {
    shared new (String s) extends JpaCtorWithoutNullarySub<X,Y,Z>(s){
        
    }
    shared new clone(JpaCtorWithoutNullary other) extends JpaCtorWithoutNullarySub<X,Y,Z>.clone(other) {
        
    }
}

shared abstract class JpaCtorWithNullarySub<X,Y,Z> extends JpaCtorWithNullary {
    shared new (String s) extends JpaCtorWithNullary(){
        
    }
    shared new clone(JpaCtorWithNullary other) extends JpaCtorWithNullary(other) {
        
    }
}
shared class JpaCtorWithNullarySubSub<X,Y,Z> extends JpaCtorWithNullarySub<X,Y,Z> {
    shared new (String s) extends JpaCtorWithNullarySub<X,Y,Z>(s){
        
    }
    shared new clone(JpaCtorWithNullary other) extends JpaCtorWithNullarySub<X,Y,Z>.clone(other) {
        
    }
}