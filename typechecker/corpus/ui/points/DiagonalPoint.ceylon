shared class DiagonalPoint(Decimal distance) 
        extends Point() {
    Decimal pos = distance / 2**0.5; 
    x = pos; 
    y = pos;
    assert ("must have distance " distance " from origin") 
        that ( x**2 + y**2 == distance**2 );
}