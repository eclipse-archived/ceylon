shared extension class Shuffleable<R,P...,Q...>(R this(P... p)(Q... q)) {
   R shuffle(Q... q)(P... p) {
       R shuffled(P... p) {
           return this(p)(q);
       }
       return shuffled;
   }
}