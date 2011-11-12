doc "Abstraction of types which support a unary inversion
     operation. For a numeric type, this should return the 
     negative of the argument value. Note that the type 
     parameter of this interface is not restricted to be a 
     self type, in order to accommodate the possibility of 
     types whose inverse can only be expressed in terms of a 
     wider type. For example, the negative of a `Natural` is 
     an `Integer`."
see (Natural, Integer, Float)
by "Gavin"
shared interface Invertable<Inverse> {
    
    doc "The inverse of the value, which may be expressed
         as an instance of a wider type."
    shared formal Inverse negativeValue;
    
    doc "The value itself, expressed as an instance of the
         wider type."
    shared formal Inverse positiveValue;

}