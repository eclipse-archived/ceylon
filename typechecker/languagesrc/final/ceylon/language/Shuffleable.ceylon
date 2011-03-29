doc "Swaps the first and second parameter lists of a function with multiple parameter lists,
     transforming a |Callable<Callable<R,Q...>,P...>| into a |Callable<Callable<R,P...>,Q...>|."
shared extension class Shuffleable<R,P...,Q...>(R this(P... p)(Q... q)) {
   R shuffle(Q... q)(P... p) {
       R shuffled(P... p) {
           return this(p)(q);
       }
       return shuffled;
   }
}