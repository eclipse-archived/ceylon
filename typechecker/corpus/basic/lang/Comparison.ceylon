doc "The receiving object is larger than 
     the given object."
shared object larger extends Comparison() {}

doc "The receiving object is smaller than 
     the given object."
shared object smaller extends Comparison() {}

doc "The receiving object is exactly equal 
     to the given object."
shared object equal extends Comparison() {}

doc "The receiving object is not comparable 
     to the given object."
shared object uncomparable extends PartialComparison() {}

doc "The result of a comparison between two
     |PartlyComparable| objects."
partial abstract class PartialComparison
        of uncomparable | Comparison
        extends Case() {}

doc "The result of a comparison between two
     |Comparable| objects."
shared abstract class Comparison() 
        of larger | smaller | equal 
        extends PartialComparison() {}