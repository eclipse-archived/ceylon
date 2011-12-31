class Pair<X,Y>(X x, Y y) {}

class Complex(Float x, Float y) 
        extends Pair<Float, Float>(x,y) {}

interface List<X> {}

class ConcreteList<X>(X... xs) 
        satisfies List<X> {}

