doc "The type of the `null` value. Any union type of form 
     `Null|T` is considered an optional type, whose values
     include `null`. Any type of this form may be written as
     `T?` for convenience."
see (null)
by "Gavin" 
shared abstract class Null() 
        of null
        extends Anything() {}

doc "The null value."
by "Gavin"
shared object null extends Null() {}
        