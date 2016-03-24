# Optimizations

The Ceylon specification permits compilers to make optimizations where 
these do not affect the semantics of the program being compiled. On the JVM 
those semantics are interpreted as follows:

* All computations performed by code compiled with 
  optimizations **must** have the same results as the same computations performed
  with unoptimized bytecode.
  
* The results **must** be the same even in the 
  event of numeric overflow during the execution of the unoptimized version. 

* If a `ceylon.language::Exception` (though not a JVM `java.lang.Error`) propagates from the unoptimized 
  code then the same type of `ceylon.language::Exception` **must** propagate from the optimized 
  code. The exception messages **must** be equal. The exception stacktraces 
  **may not** be the same.

* If and only if the unoptimized version results in a JVM `java.lang.Error` being thrown (for example 
`OutOfMemoryError`) then execution of the optimized version **may**
also result in a JVM `java.lang.Error` (though it need not be the same type of `java.lang.Error`). 

* The optimized version **should** *usually* execute in less time. 

* The optimized version **should never** execute in substantially more time.

## Boxed primitive types

TODO

## Power unrolling

Given an expression of the form:

    base^power
    
the compiler will use inline multiplication instead of invoking `base.power(power)` if:

* the static type of `base` is `ceylon.language::Integer` or `ceylon.language::Float` and
* `power` is a `NaturalLiteral` (i.e. power is a strictly positive `ceylon.language::Integer` literal) and
* the value of `power` is less than a certain implementation-defined maximum 
(to prevent code bloat with expressions like `2^1_000_000_000`).

This means that `x^3` is no slower than `x*x*x`.


## Iteration using `for`

### `for (element in start..end)`

Given a `for` statement of the form:

    for (element in start..end) {
    
    }

where 

* the static type of the `(start..end)` expression is `Range<Integer>` or `Range<Character>`
   
the compiler will emit a C-style `for` loop using a JVM primitive counter 
instead of using the usual `Iterable` contract.

**TODO When arguments are literals try to use an `int` counter if we can prove there's no overflow**

### `for (element in (start..end).by(step))`

Given a `for` statement of the form:

    for (element in (start..end).by(step)) {
    
    }

where 

* the static type of the `(start..end)` expression is `Range<Integer>` or `Range<Character>`
   
the compiler will emit a C-style `for` loop using a JVM primitive counter 
instead of using the usual `Iterable` contract.

**TODO When arguments are literals try to use an `int` counter if we can prove there's no overflow**

### `for (element in range)` or `for (element in range.by(step))`

Given a `for` statement of the form:

    for (element in range) {
    
    }
    
or 

    for (element in range.by(step)) {
    
    }
    
where:

* the static type of `range` has `Range` as a supertype

the compiler will emit a C-style `for` loop accessing `successor` or `predecessor`
instead of using the usual `Iterable` contract.


### `for (element in arraySequence)`

Given a `for` statement of the form:

    for (element in arraySequence) {
    
    }

where:

* the static type of `arraySequence` has `ArraySequence` as a supertype

the compiler will emit a C-style `for` loop using a primitive counter and
indexed access to the `ArraySequence` instead of using the usual `Iterable` 
contract.

### `for (element in array)`

Given a `for` statement of the form:

    for (element in arraySequence) {
    
    }

where:

* the static type of `array` has `Array` as a supertype

the compiler will emit a C-style `for` loop using a primitive counter and
indexed access to the `Array` instead of using the usual `Iterable` 
contract.

### `for (element in javaArray.array)`

Given a `for` statement of the form:

    for (element in javaArray.array) {
    
    }
    
where:

* the static type of `javaArray` is a JVM array virtual type (e.g. `java.lang::IntArray` which is erased to a JVM `int[]`)
    
the compiler will emit a C-style `for` loop using indexed access instead of using the usual 
`Iterable` contract.

### `for (element in iterable)`

Given a `for` statement of the form:

    for (element in iterable) {
    
    }
    
where:

* the static type of `iterable`

TODO
