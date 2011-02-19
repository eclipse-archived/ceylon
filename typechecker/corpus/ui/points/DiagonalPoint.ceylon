shared class DiagonalPoint(Float distance) 
        extends Point() {
    Float pos = distance / 2.0**0.5; 
    x = pos; 
    y = pos;
    assert ("must have distance " distance " from origin") 
        that ( x**2.0 + y**2.0 == distance**2.0 );
}