doc "The type of the `null` value. Any union type of form 
     `Nothing|T` is considered an optional type, whose values
     include `null`. Any type of this form may be written as
     `T?` for convenience."
see (null)
by "Gavin" 
shared abstract class Nothing() 
        of nothing
        extends Void() {}