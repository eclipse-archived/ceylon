doc "An instance of `Float` representing 
     positive infinity \{#221E}."
shared Float infinity = 1.0/0.0;

doc "A 64-bit floating point number. A `Float` is capable of
     approximately representing numeric values between
     2<sup>-1022</sup> and (2-2<sup>-52</sup>)\{#00D7}2<sup>1023</sup>,
     along with the special values `infinity` and `-infinity`, 
     and undefined values (Not a Number). Zero is represented 
     by distinct instances `+0`, `-0`, but these instances 
     are equal. An undefined value is not equal to any other
     value, not even to itself."
shared abstract class Float()
        extends Object()
        satisfies Scalar<Float> & Exponentiable<Float,Float> & 
                  Castable<Float> {
    
    doc "Determines whether this value is undefined (that is, Not a Number or NaN).
         The undefined value has the property that it is not equal (`==`) 
         to itself, as a consequence the undefined value cannot sensibly be 
         used in most collections."
    shared Boolean undefined => this!=this;
    
    doc "Determines whether this value is infinite in magnitude
         Produces `true` for `infinity` and `-infinity`.
         Produces `false` for a finite number, `+0`, `-0`, or
         undefined."
    see(infinity, finite)
    shared Boolean infinite => 
            this==infinity || this==-infinity;
    
    doc "Determines whether this value is finite. Produces
         `false` for `infinity`, `-infinity`, and undefined."
    see(infinite, infinity)
    shared Boolean finite =>
            this!=infinity && this!=-infinity 
                    && !this.undefined;
    
    doc "The sign of this value. Produces `1` for a positive 
         number or `infinity`. Produces `-1` for a negative
         number or `-infinity`. Produces `0` for `+0`, `-0`, 
         or undefined."
    shared actual formal Integer sign;
    
    doc "Determines if this value is a positive number or
         `infinity`. Produces `false` for a negative number, 
         `+0`, `-0`, or undefined."
    shared actual formal Boolean positive;
    
    doc "Determines if this value is a negative number or
         `-infinity`. Produces `false` for a positive number, 
         `+0`, `-0`, or undefined."
    shared actual formal Boolean negative;
    
    doc "Determines if this value is a positive number,
         `+0`, or `infinity`. Produces `false` for a 
         negative number, `-0`, or undefined."
    shared formal Boolean strictlyPositive;
    
    doc "Determines if this value is a negative number,
         `-0`, or `-infinity`. Produces `false` for a
         positive number, `+0`, or undefined."
    shared formal Boolean strictlyNegative;
    
}

doc "The `Float` value of the given string representation of 
     a decimal number or `null` if the string does not represent 
     a decimal number.
     
     The syntax accepted by this method is the same as the 
     syntax for a `Float` literal in the Ceylon language except 
     that it may optionally begin with a sign character (`+` or 
     `-`)."
shared Float? parseFloat(String string) { throw; }
