"The default superclass when no superclass is explicitly
 specified using `extends`. For the sake of convenience, 
 this class inherits [[Identifiable]] along with its
 [[default definition|Identifiable.equals]] of value 
 equality. Classes which aren't `Identifiable` should 
 directly extend [[Object]]."
by ("Gavin")
tagged("Basic types")
shared abstract class Basic() 
        extends Object() satisfies Identifiable {}