"An IEEE 754 64-bit [floating point number][]. A `Float` is 
 capable of approximately representing numeric values 
 between:
 
 - 2<sup>-1022</sup>, approximately 
   1.79769\{#00D7}10<sup>308</sup>, and 
 - (2-2<sup>-52</sup>)\{#00D7}2<sup>1023</sup>, 
   approximately 5\{#00D7}10<sup>-324</sup>.
 
 Zero is represented by distinct instances `+0`, `-0`, but 
 these instances are equal. 
 
 In addition, the following special values exist:
 
 - [[infinity]] and `-infinity`, and 
 - undefined values (Not a Number).
 
 An undefined value is not equal to any other value, not 
 even to itself.
 
 [floating point number]: http://www.validlab.com/goldberg/paper.pdf"
shared native final class Float(float)
        extends Object()
        satisfies Scalar<Float> & 
                  Exponentiable<Float,Float> {
    
    "Determines whether this value is undefined (that is, 
     Not a Number or NaN). The undefined value has the 
     property that it is not equal (`==`) to itself, as 
     a consequence the undefined value cannot sensibly 
     be used in most collections."
    shared Boolean undefined => this!=this;
    
    "Determines whether this value is infinite in 
     magnitude. Produces `true` for `infinity` and 
     `-infinity`. Produces `false` for a finite number, 
     `+0`, `-0`, or undefined."
    see (`value infinity`, `value finite`)
    shared Boolean infinite => 
            this==infinity || this==-infinity;
    
    "Determines whether this value is finite. Produces
     `false` for `infinity`, `-infinity`, and undefined."
    see (`value infinite`, `value infinity`)
    shared Boolean finite =>
            this!=infinity && this!=-infinity 
                    && !this.undefined;
    
    "The sign of this value. Produces `1` for a positive 
     number or `infinity`. Produces `-1` for a negative
     number or `-infinity`. Produces `0` for `+0`, `-0`, 
     or undefined."
    shared actual native Integer sign;
    
    "Determines if this value is a positive number or
     `infinity`. Produces `false` for a negative number, 
     `+0`, `-0`, or undefined."
    shared actual native Boolean positive;
    
    "Determines if this value is a negative number or
     `-infinity`. Produces `false` for a positive number, 
     `+0`, `-0`, or undefined."
    shared actual native Boolean negative;
    
    "Determines if this value is a positive number, `+0`, 
     or `infinity`. Produces `false` for a negative 
     number, `-0`, or undefined."
    shared native Boolean strictlyPositive;
    
    "Determines if this value is a negative number, `-0`, 
     or `-infinity`. Produces `false` for a positive 
     number, `+0`, or undefined."
    shared native Boolean strictlyNegative;
    
    shared actual native Boolean equals(Object that);
    shared actual native Integer hash;
    shared actual native Comparison compare(Float other);
    
    shared actual native Float plus(Float other);
    shared actual native Float minus(Float other);
    shared actual native Float times(Float other);
    shared actual native Float divided(Float other);
    shared actual native Float power(Float other);
    
    shared actual native Float wholePart;
    shared actual native Float fractionalPart;
    
    shared actual native Float magnitude;
    
    shared actual native Float negativeValue;
    shared actual native Float positiveValue;
    
    shared actual Float float;
    shared actual native Integer integer;
    
    shared actual native Float timesInteger(Integer integer);    
    shared actual native Float plusInteger(Integer integer);
    
}
